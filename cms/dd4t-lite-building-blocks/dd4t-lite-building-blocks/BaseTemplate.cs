using System;
using System.Collections.Generic;
using System.Linq;
using System.Security;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Xml;
using Tridion.ContentManager;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
using Tridion.ContentManager.ContentManagement.Fields;
using Tridion.ContentManager.Publishing;
using Tridion.ContentManager.Templating;
using Tridion.ContentManager.Templating.Assembly;
using Tridion.ExternalContentLibrary.V2;

namespace DD4TLite.BuildingBlocks
{
    public abstract class BaseTemplate : ITemplate
    {
        public BaseTemplate()
        {
            Log = TemplatingLogger.GetLogger(typeof(BaseTemplate));
        }

        public BaseTemplate(TemplatingLogger log)
        {
            Log = log;
        }

        public abstract void Transform(Engine engine, Package package);

        protected void Initialize(Engine engine, Package package)
        {
            this.Package = package;
            this.Engine = engine;
        }

        protected TemplatingLogger Log
        {
            get;
            set;
        }
        protected Package Package { get; set; }
        protected Engine Engine { get; set; }

        protected Page GetPage()
        {
            String pageUri = this.GetPageUri();
            Page page = (Page)Engine.GetSession().GetObject(pageUri);
            return page;
        }

        protected String GetPageUri()
        {
            return Package.GetValue("Page.ID");
        }

        protected Component GetComponent()
        {
            String componentUri = this.GetComponentUri();
            Component component = (Component)Engine.GetSession().GetObject(componentUri);
            return component;
        }

        protected Component GetComponent(TcmUri componentUri)
        {
            return (Component)Engine.GetSession().GetObject(componentUri);
        }

        protected ComponentTemplate GetComponentTemplate(TcmUri templateUri)
        {
            return (ComponentTemplate)Engine.GetSession().GetObject(templateUri);
        }

        protected Template GetTemplate()
        {
            return (Template)Engine.GetSession().GetObject(this.GetTemplateUri());
        }

        protected String GetComponentUri()
        {
            return Package.GetValue("Component.ID");
        }

        protected TcmUri GetTemplateUri()
        {
            return Engine.PublishingContext.ResolvedItem.Template.Id;
        }

        protected ItemFields GetItems(Component component)
        {
            ItemFields fields = new ItemFields(component.Content, component.Schema);
            return fields;
        }

        protected ItemFields GetMetaData(RepositoryLocalObject item)
        {
            if (item.Metadata == null) { return null; }
            ItemFields metadataFields = new ItemFields(item.Metadata, item.MetadataSchema);
            return metadataFields;
        }

        protected IComponentPresentationList GetComponentPresentations()
        {
            Item componentListItem = Package.GetByName(Package.ComponentsName);
            if (componentListItem == null) // If the Get Components from Page TBB has not been invoked
            {
                return null;
            }
            return ComponentPresentationList.FromXml(componentListItem.GetAsString());
        }

        protected bool HasPackageValue(string key)
        {
            foreach (KeyValuePair<string, Item> kvp in Package.GetEntries())
            {
                if (kvp.Key.Equals(key))
                {
                    return true;
                }
            }
            return false;
        }

        protected string AddMultiMediaComponentToPackage(Component multimediaComponent)
        {
            Item multimediaItem = Package.CreateMultimediaItem(multimediaComponent.Id);

            // Make the item name the file name (can be null)
            string itemName;
            multimediaItem.Properties.TryGetValue(Item.ItemPropertyFileName, out itemName);

            Item existingItem = Package.GetByName(itemName);
            if (
                existingItem == null ||
                !existingItem.Properties[Item.ItemPropertyTcmUri].Equals(multimediaComponent.Id) ||
                !existingItem.Equals(multimediaItem) // Ensure that a transformed item is not considered the same
            )
            {
                Package.PushItem(itemName, multimediaItem);
            }

            Publication publication = (Publication)multimediaComponent.ContextRepository;
            String imageFullPath = publication.MultimediaUrl + "/" + itemName;
            return imageFullPath;
        }

        protected bool IsSiteEditEnabled()
        {
            PublicationTarget pubTarget = this.Engine.PublishingContext.PublicationTarget as PublicationTarget;
            if (pubTarget != null)
            {
                XmlElement xappData = pubTarget.LoadApplicationData("SiteEdit").GetAs<XmlElement>();
                string siteEdit = xappData["PublicationTarget"]["EnableSiteEdit"].InnerText;
                return Boolean.Parse(siteEdit);
            }
            return true;
        }



        protected string UnicodeEscapeString(string str)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.Length; i++)
            {
                char c = str[i];

                // Escape all characters above standard 7-bit ascii
                //
                if (c > 127)
                {
                    sb.Append("\\u" + String.Format("{0:x4}", (int)c));
                }
                else
                {
                    sb.Append(c);
                }
            }

            return sb.ToString();
        }

        /// <summary>
        /// Perform XML escaping (replacement of special characters with character entities) on a string.
        /// </summary>
        /// <param name="src">The string to escape</param>
        /// <returns>The escaped string</returns>
        protected string EscapeXml(string src)
        {
            return SecurityElement.Escape(src);
        }

        /// <summary>
        /// Check if current Tridion item is published to specified publication target
        /// </summary>
        /// <param name="item"></param>
        /// <param name="publicationTarget"></param>
        /// <returns></returns>
        protected bool IsPublishedToTarget(RepositoryLocalObject item, PublicationTarget publicationTarget)
        {
            Boolean isPublished = false;
            ICollection<PublishInfo> publishInfoList = PublishEngine.GetPublishInfo(item);
            foreach (PublishInfo publishInfo in publishInfoList)
            {
                if (publishInfo.PublicationTarget.Equals(publicationTarget))
                {
                    isPublished = true;
                    break;
                }
            }
            return isPublished;
        }

        /// <summary>
        /// Return absolute path of a specified structure group item.
        /// </summary>
        /// <param name="page"></param>
        /// <returns></returns>
        protected String GetAbsolutePath(StructureGroup directory)
        {
            String path = directory.Directory + "/";

            while (directory != null && directory.Directory != "")
            {
                directory = (StructureGroup)directory.OrganizationalItem;
                if (directory.Title == "")
                {
                    path = "/" + path;
                }
                else
                {
                    path = directory.Directory + "/" + path;
                }
            }

            return path;
        }

        protected String GetParentAbsolutePath()
        {
            return GetAbsolutePath((StructureGroup)GetPage().OrganizationalItem);
        }

        protected void SetSharedParameter(String name, Object value)
        {
            Session session = Engine.GetSession();
            if (session.ContextData.ContainsKey(name))
            {
                session.ContextData.Remove(name);
            }
            session.ContextData.Add(name, value);
        }

        protected Object getSharedParameter(String name)
        {
            Session session = Engine.GetSession();
            if (session.ContextData.ContainsKey(name))
            {
                return session.ContextData[name];
            }
            return null;
        }

        protected string GetQuotedString(String value)
        {
            return "\"" + value + "\"";
        }

        protected string GetQuotedString(int value)
        {
            return "\"" + value + "\"";
        }

        protected string GetQuotedString(bool value)
        {
            return "\"" + value.ToString().ToLower() + "\"";
        }

        protected void AddOutputToPackage(StringBuilder sb)
        {
            Item componentPresentation = Package.CreateHtmlItem(sb.ToString());
            componentPresentation.Properties.Add(Item.ItemPropertyTcmUri, this.GetComponentUri());
            Package.PushItem("Output", componentPresentation);
        }

        protected void OutputMetdataFields(RepositoryLocalObject item, StringBuilder sb)
        {
            if (item.Metadata != null )
            {
                ItemFields metadata = this.GetMetaData(item);
                if (metadata.Count() > 0)
                {
                    sb.Append("<metadata>\n");
                    this.OutputFields(metadata, sb, true);
                    sb.Append("</metadata>\n");
                }
            }
        }

        protected void OutputFields(ItemFields fields, StringBuilder sb, Boolean isMetadata = false)
        {
            if (fields == null) return;

            foreach (ItemField field in fields)
            {
                if (isMetadata && field.Name.Equals("innerRegion"))
                {
                    // Exclude inner region parameter from the template data
                    //
                    continue;
                }
                sb.Append("<field name=");
                sb.Append(GetQuotedString(field.Name));
                sb.Append(" type=");
                sb.Append(GetQuotedString(GetFieldType(field)));
                sb.Append(" multivalue=");
                sb.Append(GetQuotedString(field.Definition.MaxOccurs != 1));
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
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            // TODO: How to resolve component links???
            foreach (string xhtmlValue in field.Values)
            {
                sb.Append("<xhtml>\n<![CDATA[");
                sb.Append(TemplateUtilities.ResolveRichTextFieldXhtml(xhtmlValue));
                sb.Append("]]></xhtml>\n");
            }
        }

        private void OutputTextValues(TextField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            foreach (string textValue in field.Values)
            {
                sb.Append("<text>");
                sb.Append(textValue);
                sb.Append("</text>\n");
            }
        }

        private void OutputNumberValues(NumberField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            foreach (double numberValue in field.Values)
            {
                sb.Append("<number>");
                sb.Append(numberValue);
                sb.Append("</number>\n");
            }
        }

        private void OutputDateValues(DateField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            foreach (DateTime dateValue in field.Values)
            {
                sb.Append("<date>");
                sb.Append(dateValue.ToUniversalTime().ToString("yyyy-MM-ddTHH:mm:ssZ"));
                sb.Append("</date>\n");
            }
        }

        private void OutputEmbeddedValues(EmbeddedSchemaField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            foreach (ItemFields embeddedValue in field.Values)
            {
                sb.Append("<embedded>\n");
                this.OutputFields(embeddedValue, sb);
                sb.Append("</embedded>\n");
            }
        }

        private void OutputMultimediaValues(MultimediaLinkField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            IEclSession eclSession = SessionFactory.CreateEclSession(Engine.GetSession());

            foreach (Component mmValue in field.Values)
            {
                IEclUri eclUri = eclSession.TryGetEclUriFromTcmUri(mmValue.Id);
                /*
                if (!eclSession.HostServices.IsNullOrNullEclUri(eclUri))
                {
                    // TODO: Make a more general & elegant solution on this!!
                    sb.Append("<ecl>");
                    sb.Append(eclUri.ItemId);
                    sb.Append("</ecl>");
                }
                 * */

                // TODO: Append TCM ID as attribute here???
                sb.Append("<multimedia>");
                if (!eclSession.HostServices.IsNullOrNullEclUri(eclUri))
                {
                    sb.Append(eclUri.ItemId);
                }
                else
                {
                    sb.Append(this.AddMultiMediaComponentToPackage(mmValue));
                }
                sb.Append("</multimedia>");

            }

            eclSession.Dispose();
        }

        private void OutputComponentLinkValues(ComponentLinkField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
            foreach (Component component in field.Values)
            {
                sb.Append("<componentLink>");
                sb.Append(component.Id);
                sb.Append("</componentLink>");
            }
        }

        private void OutputKeywordValues(KeywordField field, StringBuilder sb)
        {
            if (field.Values.Count() == 0)
            {
                sb.Append("<empty/>\n");
                return;
            }
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
                type = "MultiMediaLink";
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

        protected void OutputTemplate(StringBuilder sb)
        {
            String viewName = this.Package.GetByName("viewName").GetAsString();
            this.OutputTemplate(this.GetTemplate(), viewName, sb);
        }

        protected void OutputTemplate(Template template, StringBuilder sb)
        {
            String viewName = Regex.Match(template.Content, @"<viewName>(\w+)</viewName>").Groups[1].Value;
            this.OutputTemplate(template, viewName, sb);
        }

        protected void OutputTemplate(Template template, String viewName, StringBuilder sb)
        {
            sb.Append("<template id=");
            sb.Append(GetQuotedString(template.Id));
            sb.Append(" title=");
            sb.Append(GetQuotedString(template.Title));
            sb.Append(" viewName=");
            sb.Append(GetQuotedString(viewName));
            sb.Append(" revisionDate=");
            sb.Append(GetQuotedString(template.RevisionDate.ToString("s")));
            sb.Append(">\n");
            this.OutputMetdataFields(template, sb);
            sb.Append("</template>\n");
        }

        protected void OutputRegion(Region region, StringBuilder sb)
        {
            sb.Append("<region name=");
            sb.Append(GetQuotedString(region.Name));
            sb.Append(">\n");
            this.OutputComponentPresentations(region, sb);
            if (region.Template != null)
            {
                this.OutputTemplate(region.Template, sb);
            }
            this.OutputConstraints(region, sb);
            sb.Append("</region>\n");
        }

        // Default implementation, is overriden by subclasses
        //
        protected virtual void OutputComponentPresentations(Region region, StringBuilder sb)
        {
            sb.Append("<componentPresentations>\n");
            foreach (ComponentPresentationInfo cp in region.ComponentPresentations)
            {
                sb.Append(this.Engine.RenderComponentPresentation(cp.ComponentUri, cp.TemplateUri));
            }
            sb.Append("</componentPresentations>\n");
        }

        protected void OutputConstraints(Region region, StringBuilder sb)
        {
            sb.Append("<constraints>\n");

            if (region.ComponentTypes != null)
            {
                // TODO: Decouple the constraints further in the region (link component or similiar)

                // Right now only Editable Region Constraint is implemented
                //
                sb.Append("<editableRegionConstraint minOccurs=\"");             
                sb.Append(region.MinOccurs);
                sb.Append("\" maxOccurs=\"");
                sb.Append(region.MaxOccurs);
                sb.Append("\">\n");
                sb.Append("<componentPresentationTypes>\n");
                foreach (ComponentType componentType in region.ComponentTypes)
                {
                    sb.Append("<componentPresentationType schemaId=\"");
                    sb.Append(componentType.SchemaUri);
                    sb.Append("\" templateId=\"");
                    sb.Append(componentType.TemplateUri);
                    sb.Append("\"/>\n");
                }
                sb.Append("</componentPresentationTypes>\n");
                sb.Append("</editableRegionConstraint>\n");
            }
            sb.Append("</constraints>\n");
        }

    }

}
