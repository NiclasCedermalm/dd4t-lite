using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
using Tridion.ContentManager.ContentManagement.Fields;
using Tridion.ContentManager.Templating;

namespace DD4TLite.BuildingBlocks
{
    public class ComponentPresentationInfo
    {
        private Component component;
        private ComponentTemplate template;

        public ComponentPresentationInfo(Component component, ComponentTemplate template)
        {
            this.component = component;
            this.template = template;
        }


        public TcmUri ComponentUri
        {
            get { return this.component.Id; }
        }

        public TcmUri TemplateUri
        {
            get { return this.template.Id; }
        }

        public Component Component
        {
            get { return this.component;  }
        }

        public ComponentTemplate Template
        {
            get { return this.template; }
        }

        public Region InnerRegion;
    }

    public class Region
    {
        private Component regionComponent;
        private ComponentTemplate regionTemplate;
        private ItemFields fields;
        private IList<ComponentType> componentTypes;
        private IList<ComponentPresentationInfo> componentPresentations = new List<ComponentPresentationInfo>();
        private int index = -1; // This is primarly used to make inner regions unique

        public Region(Component regionComponent) : this(regionComponent, null) {}

        public Region(Component regionComponent, int index)
            : this(regionComponent, null)
        {
            this.index = index;
        }

        public Region(Component regionComponent, ComponentTemplate regionTemplate)
        {
            this.regionComponent = regionComponent;
            this.regionTemplate = regionTemplate;
            this.fields = new ItemFields(regionComponent.Content, regionComponent.Schema);
            this.componentTypes = new List<ComponentType>();
            EmbeddedSchemaField componentTypeESField = (EmbeddedSchemaField) fields["componentTypes"];
            foreach (ItemFields componentTypeFields in componentTypeESField.Values)
            {
                this.componentTypes.Add(new ComponentType(componentTypeFields, this.regionComponent.Id.PublicationId));
            }
        }

        public bool Accept(Component component, ComponentTemplate template)
        {
            foreach (ComponentType type in this.componentTypes)
            {
                if (type.SchemaId == component.Schema.Id.ItemId && type.TemplateId == template.Id.ItemId)
                {
                    return true;
                }
            }
            return false;
        }

        public void AddComponentPresentation(ComponentPresentationInfo cp)
        {
            this.componentPresentations.Add(cp);
        }

        public IList<ComponentPresentationInfo> ComponentPresentations
        {
            get { return this.componentPresentations; }
        }

        public Component Component
        {
            get { return this.regionComponent; }
        }

        public ComponentTemplate Template
        {
            get { return this.regionTemplate; }
        }

        public string Name
        {
            get {
                if (index == -1)
                {
                    return ((TextField)this.fields["name"]).Value;
                }
                else
                {
                    return ((TextField)this.fields["name"]).Value + "-" + index;
                }
            }
        }

        public int MinOccurs
        {
            get { return (int) ((NumberField) this.fields["minOccurs"]).Value; }
        }

        public int MaxOccurs
        {
            get { return (int) ((NumberField) this.fields["maxOccurs"]).Value; }
        }

        public IList<ComponentType> ComponentTypes
        {
            get { return this.componentTypes; }
        }
    }

    public class ComponentType
    {
        private int publicationId;
        private NumberField schemaId;
        private NumberField templateId;

        public ComponentType(ItemFields fields, int publicationId)
        {
            this.publicationId = publicationId;
            this.schemaId = (NumberField) fields["schemaId"];
            this.templateId = (NumberField) fields["templateId"];
        }

        public int SchemaId
        {
            get { return (int) schemaId.Value; }
        }

        public int TemplateId
        {
            get { return (int) templateId.Value; }
        }

        public String SchemaUri
        {
            get { return "tcm:" + publicationId + "-" + schemaId + "-8"; }
        }

        public String TemplateUri
        {
            get { return "tcm:" + publicationId + "-" + templateId + "-32"; }
        }
    }
}
