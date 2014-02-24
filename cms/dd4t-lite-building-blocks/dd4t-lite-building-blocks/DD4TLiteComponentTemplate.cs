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
             Item componentPresentation = package.CreateHtmlItem(sb.ToString());
             componentPresentation.Properties.Add(Item.ItemPropertyTcmUri, this.GetComponentUri());
             package.PushItem("Output", componentPresentation);
         }

         private void OutputComponentPresentation(StringBuilder sb)
         {
             sb.Append("<componentPresentation>\n");
             this.OutputComponent(sb);
             this.OutputComponentTemplate(sb);
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

         private void OutputMetdataFields(RepositoryLocalObject item, StringBuilder sb)
         {
             sb.Append("<metadata>\n");
             this.OutputFields(this.GetMetaData(item), sb);
             sb.Append("</metadata>\n");
         }

         private void OutputFields(ItemFields fields, StringBuilder sb)
         {
             foreach (ItemField field in fields)
             {
                 sb.Append("<field name=");
                 sb.Append(GetQuotedString(field.Name));
                 sb.Append(" type=");
                 sb.Append(GetQuotedString(GetFieldType(field)));
                 sb.Append(" xpath=");
                 sb.Append(GetQuotedString("TBD")); // TODO: How to get the XPath????
                 sb.Append(">\n");
                 sb.Append("<values>\n");
                 this.OutputFieldValues(field, sb);
                 sb.Append("</values>\n");
                 sb.Append("</field>\n");                 
             }
         }

         private void OutputFieldValues(ItemField field, StringBuilder sb)
         {
             // Multiline field???

             // TODO: Can this selector be smarter by using overloading instead???

             if (field is XhtmlField)
             {
                 this.OutputXhtmlValues((XhtmlField)field, sb);
             }
             else if (field is TextField)
             {
                 this.OutputTextValues((TextField)field, sb);
             }
             else if (field is NumberField)
             {
                 this.OutputNumberValues((NumberField)field, sb);
             }
             else if (field is DateField)
             {
                 this.OutputDateValues((DateField)field, sb);
             }
             else if (field is EmbeddedSchemaField)
             {
                 this.OutputEmbeddedValues((EmbeddedSchemaField)field, sb);
             }
             else if (field is MultimediaLinkField)
             {
                 this.OutputMultimediaValues((MultimediaLinkField)field, sb);
             }
             else if (field is ComponentLinkField)
             {
                 this.OutputComponentLinkValues((ComponentLinkField)field, sb);
             }
             else if (field is KeywordField)
             {
                 this.OutputKeywordValues((KeywordField)field, sb);
             }            
         }

         private void OutputXhtmlValues(XhtmlField field, StringBuilder sb)
         {
             // TODO: Do some processing of the XHTML values???
             // TODO: Embed into CDATA????

             foreach (string xhtmlValue in field.Values)
             {
                 sb.Append("<xhtml>\n<![CDATA[");
                 sb.Append(TemplateUtilities.ResolveRichTextFieldXhtml(xhtmlValue));
                 sb.Append("]]></xhtml>\n");
             }
         }

         private void OutputTextValues(TextField field, StringBuilder sb)
         {
             foreach ( string textValue in field.Values )
             {
                 sb.Append("<text>");
                 sb.Append(textValue);
                 sb.Append("</text>\n");
             }
         }

         private void OutputNumberValues(NumberField field, StringBuilder sb)
         {
             foreach (double numberValue in field.Values)
             {
                 sb.Append("<number>");
                 sb.Append(numberValue);
                 sb.Append("</number>\n");
             }
         }

         private void OutputDateValues(DateField field, StringBuilder sb)
         {
             foreach (DateTime dateValue in field.Values)
             {
                 sb.Append("<date>");
                 sb.Append(dateValue.ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ"));
                 sb.Append("</date>\n");
             }
         }

         private void OutputEmbeddedValues(EmbeddedSchemaField field, StringBuilder sb)
         {
             foreach (ItemFields embeddedValue in field.Values)
             {
                 sb.Append("<embedded>\n");
                 this.OutputFields(embeddedValue, sb);
                 sb.Append("</embedded>\n");
             }
         }

         private void OutputMultimediaValues(MultimediaLinkField field, StringBuilder sb)
         {
             foreach (Component mmValue in field.Values)
             {
                 // TODO: Append TCM ID as attribute here???
                 sb.Append("<multimedia>");
                 sb.Append(this.AddMultiMediaComponentToPackage(mmValue));             
                 sb.Append("</multimedia>");
             }
         }

         private void OutputComponentLinkValues(ComponentLinkField field, StringBuilder sb)
         {
             foreach (Component component in field.Values)
             {
                 sb.Append("<componentLink>");
                 sb.Append(component.Id);
                 sb.Append("</componentLink>");
             }
         }

         private void OutputKeywordValues(KeywordField field, StringBuilder sb)
         {
             foreach (Keyword keyword in field.Values)
             {
                 sb.Append("<keyword taxonomyId=");
                 sb.Append(GetQuotedString(keyword.Id));
                 sb.Append(" path=");
                 sb.Append(GetQuotedString(keyword.Path));
                 sb.Append(" key=");
                 sb.Append(GetQuotedString(keyword.Key));
                 sb.Append("/>\n");
             }
         }

         private String GetFieldType(ItemField field)
         {
             String type;
             if (field is XhtmlField)
             {
                 type = "Xhtml";
             }
             else if (field is TextField)
             {
                 type = "Text";
             }
             else if (field is NumberField)
             {
                 type = "Number";
             }
             else if (field is DateField)
             {
                 type = "Date";
             }
             else if (field is EmbeddedSchemaField)
             {
                 type = "Embedded";
             }
             else if (field is MultimediaLinkField)
             {
                 type = "Multimedia";
             }
             else if (field is ComponentLinkField)
             {
                 type = "ComponentLink";
             }
             else if (field is KeywordField)
             {
                 type = "Keyword";
             }
             else
             {
                 type = "Unknown";
             }
             return type;
         }

         
         private void OutputComponentTemplate(StringBuilder sb)
         {
            
             String viewName = this.Package.GetByName("viewName").GetAsString();
            
             ComponentTemplate template = this.GetComponentTemplate();
             sb.Append("<template id=");
             sb.Append(GetQuotedString(template.Id));
             sb.Append(" title=");
             sb.Append(GetQuotedString(template.Title));
             sb.Append(" viewName=");            
             sb.Append(GetQuotedString(viewName));
             sb.Append(">\n");
             this.OutputMetdataFields(template, sb);
             sb.Append("</template>\n"); 

         }

    }
}
