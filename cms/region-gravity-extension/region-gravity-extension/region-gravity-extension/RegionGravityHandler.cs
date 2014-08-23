using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.Extensibility;
using Tridion.ContentManager.Extensibility.Events;
using Tridion.Logging;

namespace DD4TLite.Extensions.Region
{
    [TcmExtension("DD4TLite-RegionGravityHandler")]
    public class RegionGravityHandler : TcmExtension
    {
        
        public RegionGravityHandler()
        {
            EventSystem.Subscribe<Page, SaveEventArgs>(OnPageSave, EventPhases.Initiated);
        }

        // TODO: Consider some priority system between regions to get the best match instead of first match


        // TODO: Handle delete component case here???

        // TODO: Verify that all added components are put on the correct place. If it is added after a component that is used several times it is added to the first region the component is located
 
        // TODO: Consider to rebuild the page component presentation list from scratch based by iterating through the regions

        public static void OnPageSave(Page page, SaveEventArgs args, EventPhases phase)
        {
            Logger.Write("On Page Save", "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
            if ( IsRegionPage(page) )
            {
                IList<Region> regions = Region.GetRegions(page);
                Logger.Write("Regions: " + regions.Count, "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
                if (regions.Count > 0)
                {
                    // Check if last component really belongs to the last region, if not let some region gravity happen on it
                    // This is primarily to support adding components on-the-fly in regions via XPM
                    //
                    Region lastRegion = regions[regions.Count - 1];
                    bool foundNewComponent = false;
                    if (lastRegion.ComponentPresentations.Count > 0)
                    {
                        ComponentPresentationInfo lastCP = lastRegion.ComponentPresentations[lastRegion.ComponentPresentations.Count - 1];

                        if (lastCP.RegionIndex != -1 )
                        {
                            Region region = GetRegionByIndex(regions, lastCP.RegionIndex);
                            Logger.Write("Found region: " + region.Name, "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
                            if (region != null)
                            {
                                region.Add(lastCP.ComponentPresentation);

                                // Remove the last entry from the page, because now the CP is added to another region on the page
                                //
                                page.ComponentPresentations.RemoveAt(page.ComponentPresentations.Count - 1);
                                foundNewComponent = true;
                            }
                        }
                    }
                    if (!foundNewComponent)
                    {
                        Logger.Write("No new component added to the bottom...", "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
                        //ProcessComponentPresentationsInWrongRegion(page.ComponentPresentations, regions);
                    }
                    ProcessComponentTemplates(page.ComponentPresentations);

                    /*
                    if ( false ) // OLD CODE!!!
                    {

                        // Check if last component really belongs to the last region, if not let some region gravity happen on it
                        // This is primarily to support adding components on-the-fly in regions via XPM
                        //
                        Region lastRegion = regions[regions.Count - 1];

                        if (lastRegion.ComponentPresentations.Count > 0)
                        {

                            ComponentPresentationInfo lastCP = lastRegion.ComponentPresentations[lastRegion.ComponentPresentations.Count - 1];

                            if (lastRegion.CanContain(lastCP.ComponentPresentation) == false)
                            {
                                Region region = GetMatchingRegion(regions, lastCP.ComponentPresentation);
                                if (region != null)
                                {
                                    region.Add(lastCP.ComponentPresentation);

                                    // Remove the last entry from the page, because now the CP is added to another region on the page
                                    //
                                    page.ComponentPresentations.RemoveAt(page.ComponentPresentations.Count - 1);
                                }
                            }
                        }
                    }
                     * */
                }
            }
        }

        protected static bool IsRegionPage(Page page)
        {
            foreach (ComponentPresentation cp in page.ComponentPresentations)
            {
                if (cp.Component.Schema.Title.Equals("DD4T Lite Region"))
                {
                    return true;
                }
            }
            return false;
        }

        static public Region GetMatchingRegion(IList<Region> regions, ComponentPresentation componentPresentation)
        {
            foreach (Region region in regions)
            {
                if (region.CanContain(componentPresentation))
                {
                    return region;
                }
                foreach (var cp in region.ComponentPresentations)
                {
                    if (cp.InnerRegion != null)
                    {
                        if (cp.InnerRegion.CanContain(componentPresentation))
                        {
                            return cp.InnerRegion;
                        }
                    }
                }
            }
            return null;
        }

        static public void ProcessComponentPresentationsInWrongRegion(IList<ComponentPresentation> componentPresentations, IList<Region> regions)
        {
            int pageIndex = -1;
            foreach (Region region in regions)
            {
                foreach (var cp in region.ComponentPresentations)
                {
                    pageIndex++;
                    if ( cp.Owner.Index != cp.RegionIndex )
                    {
                        componentPresentations.RemoveAt(pageIndex);
                        region.Add(cp.ComponentPresentation);                 
                    }
                    if (cp.InnerRegion != null)
                    {
                        foreach (var innerCp in cp.InnerRegion.ComponentPresentations)
                        {
                            pageIndex++;
                            if (innerCp.RegionIndex != -1)
                            {
                                componentPresentations.RemoveAt(pageIndex);
                                cp.InnerRegion.Add(innerCp.ComponentPresentation);
                            }
                        }
                    }
                }
            }
        }


        static public Region GetRegionByIndex(IList<Region> regions, int index)
        {
            foreach (Region region in regions)
            {
                if (region.Index == index) { return region; }
                foreach (var cp in region.ComponentPresentations)
                {
                    if (cp.InnerRegion != null && cp.InnerRegion.Index == index)
                    {
                        return cp.InnerRegion; 
                    }
                }
            }
            return null;
        }

        static public void ProcessComponentTemplates(IList<ComponentPresentation> componentPresentations)
        {
            Logger.Write("Processing Component Templates...", "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
            foreach ( ComponentPresentation cp in componentPresentations )
            {
                if (Region.ExtractRegionIndex(cp.ComponentTemplate.Id) != -1)
                {
                    Logger.Write("Found Template URI with region index: " + cp.ComponentTemplate.Id, "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
                    TcmUri realTemplateUri = Region.RemoveRegionIndex(cp.ComponentTemplate.Id);
                    Logger.Write("Real Template URI: " + realTemplateUri, "RegionGravityHandler", LogCategory.Custom, System.Diagnostics.TraceEventType.Information);
                    cp.ComponentTemplate = new ComponentTemplate(realTemplateUri, cp.Session); 
                }
            }
        }
        
    }
}
