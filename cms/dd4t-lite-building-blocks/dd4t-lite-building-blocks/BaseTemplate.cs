using System;
using System.Collections.Generic;
using System.Linq;
using System.Security;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using Tridion.ContentManager;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.ContentManagement;
using Tridion.ContentManager.ContentManagement.Fields;
using Tridion.ContentManager.Publishing;
using Tridion.ContentManager.Templating;
using Tridion.ContentManager.Templating.Assembly;

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
            Component component = (Component) Engine.GetSession().GetObject(componentUri);
            return component;
        }

        protected Component GetComponent(TcmUri componentUri)
        {
            return (Component) Engine.GetSession().GetObject(componentUri);
        }

        protected ComponentTemplate GetComponentTemplate(TcmUri templateUri)
        {
            return (ComponentTemplate) Engine.GetSession().GetObject(templateUri);
        }

        protected ComponentTemplate GetComponentTemplate()
        {
            return (ComponentTemplate) Engine.GetSession().GetObject(this.GetTemplateUri());
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
            for (int i=0;i < str.Length; i++ )
            {
                char c = str[i];
                
                // Escape all characters above standard 7-bit ascii
                //
                if (c > 127 )
                {
                    sb.Append("\\u" + String.Format("{0:x4}", (int) c));
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
            return GetAbsolutePath((StructureGroup) GetPage().OrganizationalItem);
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

    }
}
