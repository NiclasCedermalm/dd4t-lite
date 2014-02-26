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
    [TcmTemplateTitle("DD4T Lite Component Template")]
    public class DD4TLiteComponentTemplate : BaseTemplate
    {
         public DD4TLiteComponentTemplate() : base(TemplatingLogger.GetLogger(typeof(DD4TLiteComponentTemplate))) { }

         /// <summary>
         /// Transform
         /// </summary>
         /// <param name="engine"></param>
         /// <param name="package"></param>
         public override void Transform(Engine engine, Package package)
         {
             this.Initialize(engine, package);

             StringBuilder sb = new StringBuilder();
             this.OutputComponentPresentation(sb);
             this.AddOutputToPackage(sb);
         }

         private void OutputComponentPresentation(StringBuilder sb)
         {
             sb.Append("<componentPresentation>\n");
             this.OutputComponent(sb);
             this.OutputTemplate(sb);
             sb.Append("</componentPresentation>\n");
         }
         
         private void OutputComponent(StringBuilder sb)
         {
             Component component = this.GetComponent();
             sb.Append("<component id=");
             sb.Append(GetQuotedString(component.Id));
             sb.Append(" title=");
             sb.Append(GetQuotedString(component.Title));
             sb.Append(">\n");
             this.OutputContentFields(component, sb);
             sb.Append("</component>\n");
         }

         private void OutputContentFields(Component component, StringBuilder sb)
         {
             sb.Append("<content>\n");
             this.OutputFields(this.GetItems(component), sb);
             sb.Append("</content>\n");
         }

    }
}
