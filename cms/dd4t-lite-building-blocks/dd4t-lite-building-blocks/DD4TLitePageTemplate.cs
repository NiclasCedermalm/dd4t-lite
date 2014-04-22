using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
using Tridion.ContentManager.ContentManagement.Fields;
using Tridion.ContentManager.Templating;
using Tridion.ContentManager.Templating.Assembly;

namespace DD4TLite.BuildingBlocks
{
    [TcmTemplateTitle("DD4T Lite Page Template")]
    class DD4TLitePageTemplate : BaseTemplate
    {
        public DD4TLitePageTemplate() : base(TemplatingLogger.GetLogger(typeof(DD4TLitePageTemplate))) { }

        /// <summary>
        /// Transform
        /// </summary>
        /// <param name="engine"></param>
        /// <param name="package"></param>
        public override void Transform(Engine engine, Package package)
        {
            this.Initialize(engine, package);

            // TODO: Obey different region strategies, such region-as-component, region-as-template-metadata, noregion

            StringBuilder sb = new StringBuilder();
            this.OutputPage(sb);
            this.AddOutputToPackage(sb);
        }

        private void OutputPage(StringBuilder sb)
        {
            Page page = this.GetPage();
            sb.Append("<page id=");
            sb.Append(GetQuotedString(page.Id));
            sb.Append(" title=");
            sb.Append(GetQuotedString(page.Title));
            sb.Append(" revisionDate=");
            sb.Append(GetQuotedString(page.RevisionDate.ToString("s")));
            sb.Append(">\n");
            this.OutputRegions(sb);
            this.OutputTemplate(sb);
            this.OutputMetdataFields(page, sb);
            sb.Append("</page>");
        }

        private void OutputRegions(StringBuilder sb)
        {
            sb.Append("<regions>\n");
            IComponentPresentationList componentPresentations = this.GetComponentPresentations();
         
            Region region = null;
            Region innerRegion = null;

            foreach (Tridion.ContentManager.Templating.ComponentPresentation componentPresentation in componentPresentations)
            {
                Component component = new Component(componentPresentation.ComponentUri, Engine.GetSession());
                ComponentTemplate template = new ComponentTemplate(componentPresentation.TemplateUri, Engine.GetSession());
                Log.Debug("Checking component of type: " + component.Schema.Title);

                // TODO: How to handle region schemas?? What pattern to look for??? Find a more generic approach than looking on the schema title
        
                if (component.Schema.Title.Equals("DD4T Lite Region"))
                {
                    if (region != null)
                    {
                        sb.Append(this.RenderRegion(region));
                    }
                    region = new Region(component, template);
                    innerRegion = null;
                }
                else if ( innerRegion != null && innerRegion.Accept(component, template) )
                {
                    innerRegion.AddComponentPresentation(new ComponentPresentationInfo(component, template));
                }
                else if (region != null)
                {
                    innerRegion = null; // Clear inner region
                    Log.Debug("Adding component: " + component.Title + " to region: " + region.Name);
                    ComponentPresentationInfo cpInfo = new ComponentPresentationInfo(component, template);
                    region.AddComponentPresentation(cpInfo);
                    if (this.IsContainerComponent(template))
                    {
                        innerRegion = new Region(this.GetInnerRegion(template));
                        cpInfo.InnerRegion = innerRegion;
                    }
                }
            }
            if (region != null)
            {
                Log.Debug("Outputting region: " + region.Name);            
                sb.Append(this.RenderRegion(region));
            }
            sb.Append("</regions>\n");
        }

        private bool IsContainerComponent(ComponentTemplate template)
        {
            ItemFields metadata = this.GetMetaData(template);
            return metadata != null && metadata.Contains("innerRegion") && metadata["innerRegion"] != null;
        }

        private Component GetInnerRegion(ComponentTemplate template)
        {
            Component innerRegion = null;
            ItemFields metadata = this.GetMetaData(template);
            ComponentLinkField innerRegionField = (ComponentLinkField) metadata["innerRegion"];
            if (innerRegionField != null)
            {
                innerRegion = innerRegionField.Value;
            }
            return innerRegion;
        }

        private string RenderRegion(Region region)
        {
            this.SetSharedParameter("region", region);
            return this.Engine.RenderComponentPresentation(region.Component.Id, region.Template.Id);
        }
    }
}
