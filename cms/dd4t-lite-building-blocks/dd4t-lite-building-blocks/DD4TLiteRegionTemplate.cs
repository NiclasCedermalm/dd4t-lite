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
    [TcmTemplateTitle("DD4T Lite Region Template")]
    class DD4TLiteRegionTemplate : BaseTemplate
    {
        public DD4TLiteRegionTemplate() : base(TemplatingLogger.GetLogger(typeof(DD4TLiteRegionTemplate))) { }

        /// <summary>
        /// Transform
        /// </summary>
        /// <param name="engine"></param>
        /// <param name="package"></param>
        public override void Transform(Engine engine, Package package)
        {
            this.Initialize(engine, package);

            StringBuilder sb = new StringBuilder();
            this.OutputRegion(sb);
            this.AddOutputToPackage(sb);
        }

        private void OutputRegion(StringBuilder sb)
        {
            Component region = this.GetComponent();
            ItemFields regionFields = this.GetItems(region);
            sb.Append("<region name=");
            sb.Append(GetQuotedString(regionFields["name"].ToString()));
            sb.Append(">\n");
            this.OutputComponentPresentations(sb);
            this.OutputTemplate(sb);
            sb.Append("</region>\n");
        }


        private void OutputComponentPresentations(StringBuilder sb)
        {
            // TODO: How to output component presentations???
            // Have some kind of flexible and pluggable region strategy that handles this???
            sb.Append("<componentPresentations>\n");
            IComponentPresentationList regionComponents = (IComponentPresentationList)this.getSharedParameter("regionComponents");
            if (regionComponents != null)
            {
                foreach (Tridion.ContentManager.Templating.ComponentPresentation cp in regionComponents)
                {
                    // TODO: Handle dynamic components here aswell
                    sb.Append(this.Engine.RenderComponentPresentation(cp.ComponentUri, cp.TemplateUri));
                }
            }
            sb.Append("</componentPresentations>\n");
        }

    }
}
