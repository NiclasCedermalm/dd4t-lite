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

            Region region = (Region) this.getSharedParameter("region");

            StringBuilder sb = new StringBuilder();
            this.OutputRegion(region, sb);
            this.AddOutputToPackage(sb);
        }

        protected override void OutputComponentPresentations(Region region, StringBuilder sb)
        {
            // TODO: How to output component presentations???
            // Have some kind of flexible and pluggable region strategy that handles this???
            sb.Append("<componentPresentations>\n");
 
            foreach (ComponentPresentationInfo cp in region.ComponentPresentations)
            {
                // TODO: Handle dynamic components here aswell
                this.SetSharedParameter("innerRegion", cp.InnerRegion);
                sb.Append(this.Engine.RenderComponentPresentation(cp.ComponentUri, cp.TemplateUri));
            }

            sb.Append("</componentPresentations>\n");
        }

    }
}
