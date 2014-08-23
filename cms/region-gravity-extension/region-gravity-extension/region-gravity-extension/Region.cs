using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
using Tridion.ContentManager.ContentManagement.Fields;
using Tridion.Logging;

namespace DD4TLite.Extensions.Region
{
    public class Region
    {
        protected Page page;
        protected Component regionComponent;
        private ItemFields regionFields;
        private int index;
        private int pageIndex;

        static public IList<Region> GetRegions(Page page)
        {
            int regionCount = 0;
            int pageIndex = 0;
            IList<Region> regions = new List<Region>();
            Region currentRegion = null;
            InnerRegion innerRegion = null;

            // TODO: Have a better page index calculation algorithm that can handle reshuffle of component presentations
            //

            foreach (ComponentPresentation cp in page.ComponentPresentations )
            {             
                if ( IsRegion(cp) )
                {                  
                    currentRegion = new Region(page, cp.Component);
                    currentRegion.Index = ++regionCount;
                    currentRegion.pageIndex = pageIndex;
                    regions.Add(currentRegion);
                }
                else if (innerRegion != null && innerRegion.CanContain(cp))
                {
                    innerRegion.AddToComponentPresentationList(cp);
                }
                else
                {
                    if (currentRegion != null)
                    {
                        ComponentPresentationInfo cpInfo = currentRegion.AddToComponentPresentationList(cp);
                        innerRegion = cpInfo.InnerRegion;
                        if (innerRegion != null)
                        {
                            innerRegion.Index = ++regionCount;
                            innerRegion.pageIndex = pageIndex;
                        }
                    }
                }
                pageIndex++; // This works only if we move the last item around....
            }
       
            return regions;
        }

        static public bool IsRegion(ComponentPresentation componentPresentation)
        {
            return componentPresentation.Component.Schema.Title.Equals("DD4T Lite Region");
        }

        static public int ExtractRegionIndex(ComponentPresentationInfo componentPresentation)
        {
            return ExtractRegionIndex(componentPresentation.ComponentPresentation.ComponentTemplate.Id);
        }

        static public int ExtractRegionIndex(TcmUri templateUri)
        {
            String itemId = templateUri.ItemId.ToString();
            if (itemId.Length > 6)
            {
                String regionId = itemId.Substring(itemId.Length - 3);
                return Int32.Parse(regionId);
            }
            return -1;
        }

        static public TcmUri RemoveRegionIndex(TcmUri templateUri)
        {
            String itemId = templateUri.ItemId.ToString();
            itemId = itemId.Substring(0, itemId.Length - 3);
            return new TcmUri("tcm:" + templateUri.PublicationId + "-" + itemId + "-32");
        }

        protected Region(Page page, Component regionComponent)
        {
            this.page = page;
            this.regionComponent = regionComponent;
            this.ComponentPresentations = new List<ComponentPresentationInfo>();
            this.regionFields = new ItemFields(this.regionComponent.Content, this.regionComponent.Schema);
        }

        public String Name 
        {
            get { return this.regionComponent.Title; }
        }

        public IList<ComponentType> ComponentTypes
        {
            get 
            {
                IList<ComponentType> componentTypeList = new List<ComponentType>();
                int publicationId = regionComponent.Id.PublicationId;
                EmbeddedSchemaField componentTypes = (EmbeddedSchemaField) regionFields["componentTypes"];
                foreach (ItemFields componentType in componentTypes.Values)
                {
                    NumberField schema = (NumberField)componentType["schemaId"];
                    NumberField template = (NumberField)componentType["templateId"];
                    TcmUri schemaUri =  new TcmUri("tcm:" + publicationId + "-" + (int) schema.Value + "-8");
                    TcmUri templateUri =  new TcmUri("tcm:" + publicationId + "-" + (int) template.Value + "-32");
                    componentTypeList.Add(new ComponentType(schemaUri, templateUri));
                }
                return componentTypeList;
            }
        }

        public int MinOccurs
        {
            get
            {
                return (int) ((NumberField)regionFields["minOccurs"]).Value;
            }
        }

        public int MaxOccurs
        {
            get
            {
                return (int)((NumberField)regionFields["maxOccurs"]).Value;
            }
        }

        public int Index
        {
            get { return this.index; }
            private set { this.index = value; }
        }

        public IList<ComponentPresentationInfo> ComponentPresentations { get; private set; }

        public IList<ComponentPresentation> GetNonMatchingComponentPresentations()
        {
            IList<ComponentPresentation> nonMatchingList = new List<ComponentPresentation>();
            foreach (ComponentPresentationInfo cp in this.ComponentPresentations)
            {
                if (this.CanContain(cp.ComponentPresentation) == false)
                {
                    nonMatchingList.Add(cp.ComponentPresentation);
                }
            }
            return nonMatchingList;
        }

        public bool CanContain(ComponentPresentation componentPresentation)
        {
            if (this.ComponentPresentations.Count < this.MaxOccurs)
            {
                foreach (ComponentType componentType in this.ComponentTypes)
                {
                    if (componentPresentation.Component.Schema.Id.Equals(componentType.SchemaUri) &&
                         componentPresentation.ComponentTemplate.Id.Equals(componentType.TemplateUri))
                    {
                        return true;
                    }

                }
            }
            return false;
        }

        private ComponentPresentationInfo AddToComponentPresentationList(ComponentPresentation componentPresentation)
        {
            ComponentPresentationInfo cpInfo = new ComponentPresentationInfo(this.page, componentPresentation, this);
            this.ComponentPresentations.Add(cpInfo);
            return cpInfo;
        }

        public virtual void Add(ComponentPresentation componentPresentation)
        {
            Logger.Write("Adding CP to page index: " + pageIndex, "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
            this.AddToComponentPresentationList(componentPresentation);
            page.ComponentPresentations.Insert(pageIndex + this.ComponentPresentations.Count(), componentPresentation);
            
        }

    }

    public class InnerRegion : Region
    {
        public InnerRegion(Page page, Component regionComponent, Component containerComponent) : base(page, regionComponent) 
        {
            this.ContainerComponent = containerComponent;
        }

        public Component ContainerComponent { get; private set; }

        /*
        public override void Add(ComponentPresentation componentPresentation)
        {
            int pageIndex = 0;
            bool foundMyself = false;
            foreach (ComponentPresentation cp in this.page.ComponentPresentations)
            {
                if (cp.Component.Equals(this.ContainerComponent))
                {
                    foundMyself = true;
                }
                else if (foundMyself )
                {
                    // TODO: This insert the CP first in the region. Correct really???
                    // The extension is probably only used for the first component in the region...
                    //
                    page.ComponentPresentations.Insert(pageIndex, componentPresentation);

                    break;
                }
                pageIndex++;
            }
        }
         * */
    }

    public class ComponentType
    {
        public ComponentType(TcmUri schemaUri, TcmUri templateUri)
        {
            SchemaUri =schemaUri;
            TemplateUri = templateUri;
        }

        public TcmUri SchemaUri { get; private set; }
        public TcmUri TemplateUri { get; private set; }
    }

    public class ComponentPresentationInfo
    {

        public ComponentPresentationInfo(Page page, ComponentPresentation componentPresentation, Region owner)
        {
            ComponentPresentation = componentPresentation;
            InnerRegion = GetInnerRegion(page, componentPresentation.ComponentTemplate);
            RegionIndex = Region.ExtractRegionIndex(componentPresentation.ComponentTemplate.Id);
            Owner = owner;
        }

        public ComponentPresentation ComponentPresentation { get; private set; }
        public InnerRegion InnerRegion { get; private set; }
        public int RegionIndex { get; private set; }
        public Region Owner { get; private set; }
 
        private InnerRegion GetInnerRegion(Page page, ComponentTemplate template)
        {
            if (Region.ExtractRegionIndex(template.Id) == -1 && template.Metadata != null)
            {
                ItemFields metadata = new ItemFields(template.Metadata, template.MetadataSchema);
                if (metadata != null && metadata.Contains("innerRegion") && metadata["innerRegion"] != null)
                {
                    ComponentLinkField innerRegionField = (ComponentLinkField) metadata["innerRegion"];
                    return new InnerRegion(page, innerRegionField.Value, ComponentPresentation.Component); 
                }
            }
            return null;
        }

    }
}
