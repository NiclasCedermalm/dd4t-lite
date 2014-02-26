using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
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
            // TODO: Output revision date here!!!
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
            IComponentPresentationList regionComponents = null;
            Tridion.ContentManager.Templating.ComponentPresentation region;

            foreach (Tridion.ContentManager.Templating.ComponentPresentation componentPresentation in componentPresentations)
            {
                Component component = new Component(componentPresentation.ComponentUri, Engine.GetSession());
                Log.Debug("Checking component of type: " + component.Schema.Title);

                // TODO: How to handle region schemas?? What pattern to look for??? Find a more generic approach than looking on the schema title
        
                if (component.Schema.Title.Equals("DD4T Lite Region"))
                {
                    if (regionComponents != null)
                    {
                        this.OutputRegion(region, regionComponents, sb);
                        regionComponents = null;
                    }
                    region = componentPresentation;
                    regionComponents = new ComponentPresentationList();
                }
                else if (regionComponents != null)
                {
                    regionComponents.Add(componentPresentation);
                }
            }
            if (regionComponents != null)
            {
                this.OutputRegion(region, regionComponents, sb);
            }
            sb.Append("</regions>\n");
        }

        private void OutputRegion(Tridion.ContentManager.Templating.ComponentPresentation region, IComponentPresentationList regionComponents, StringBuilder sb)
        {
            Log.Debug("Outputting region...");
            this.SetSharedParameter("regionComponents", regionComponents);
            sb.Append(this.Engine.RenderComponentPresentation(region.ComponentUri, region.TemplateUri));
        }

    }
}
