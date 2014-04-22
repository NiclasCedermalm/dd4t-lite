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
using Tridion.ExternalContentLibrary.V2;

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
             Region innerRegion = (Region) this.getSharedParameter("innerRegion");
             if (innerRegion != null)
             {
                 this.OutputRegion(innerRegion, sb);
             }
             
             sb.Append("</componentPresentation>\n");
         }

         private void OutputComponent(StringBuilder sb)
         {
             this.OutputComponent(this.GetComponent(), sb);
         }

         private void OutputComponent(Component component, StringBuilder sb)
         {
             Log.Debug("Outputting component: " + component.Id);
             sb.Append("<component id=");
             sb.Append(GetQuotedString(component.Id));
             sb.Append(" title=");
             sb.Append(GetQuotedString(component.Title));
             sb.Append(" revisionDate=");
             sb.Append(GetQuotedString(component.RevisionDate.ToString("s")));
             sb.Append(">\n");
             this.OutputContentFields(component, sb);
             this.OutputSchema(component.Schema, sb);
             sb.Append("</component>\n");
          
             if ( !component.Title.StartsWith("ecl:") && component.ComponentType == Tridion.ContentManager.ContentManagement.ComponentType.Multimedia)
             {
                 this.AddMultiMediaComponentToPackage(component);
             }
             
         }

         private void OutputContentFields(Component component, StringBuilder sb)
         {
             if (component.ComponentType == Tridion.ContentManager.ContentManagement.ComponentType.Multimedia && component.Title.StartsWith("ecl:"))
             {
                 // If an ECL component -> output ECL data
                 //
                 IEclSession eclSession = SessionFactory.CreateEclSession(Engine.GetSession());
                 IEclUri eclUri = eclSession.TryGetEclUriFromTcmUri(component.Id);
                 sb.Append("<content>\n");
                 sb.Append("<field name=\"eclId\" type=\"Text\" multivalue=\"false\"><values><text>");
                 sb.Append(eclUri);
                 sb.Append("</text></values></field>\n");
                 sb.Append("<field name=\"itemId\" type=\"Text\" multivalue=\"false\"><values><text>");
                 sb.Append(eclUri.ItemId);
                 sb.Append("</text></values></field>\n");
                 sb.Append("</content>\n");
                 eclSession.Dispose();
             }
             else if (component.ComponentType != Tridion.ContentManager.ContentManagement.ComponentType.Multimedia)
             {
                 sb.Append("<content>\n");
                 this.OutputFields(this.GetItems(component), sb);
                 sb.Append("</content>\n");
             }
         }

         private void OutputSchema(Schema schema, StringBuilder sb)
         {
             sb.Append("<schema id=");
             sb.Append(GetQuotedString(schema.Id.ToString()));
             sb.Append(" title=");
             sb.Append(GetQuotedString(schema.Title));
             sb.Append(" rootElementName=");
             sb.Append(GetQuotedString(schema.RootElementName));
             sb.Append("/>\n");
         }


         /// <summary>
         /// Render inner component presentations
         /// </summary>
         /// <param name="region"></param>
         /// <param name="sb"></param>
         protected override void OutputComponentPresentations(Region region, StringBuilder sb)
         {           
             sb.Append("<componentPresentations>\n");
             foreach (ComponentPresentationInfo cp in region.ComponentPresentations)
             {
                 sb.Append("<componentPresentation>\n");
                 this.OutputComponent(cp.Component, sb);
                 this.OutputTemplate(cp.Template, sb);
                 sb.Append("</componentPresentation>\n");
             }
             sb.Append("</componentPresentations>\n");
         }

    }
}
