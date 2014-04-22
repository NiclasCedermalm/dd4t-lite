using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Tridion.ContentManager.CommunicationManagement;
using Tridion.ContentManager.Extensibility;
using Tridion.ContentManager.Extensibility.Events;
using Tridion.Logging;

namespace DD4TLite.Extensions.Region
{
    [TcmExtension("RegionGravityHandler")]
    public class RegionGravityHandler : TcmExtension
    {
        
        public RegionGravityHandler()
        {
            EventSystem.Subscribe<Page, SaveEventArgs>(OnPageSave, EventPhases.Initiated);
        }

        // TODO: Consider some priority system between regions to get the best match instead of first match
       
        public static void OnPageSave(Page page, SaveEventArgs args, EventPhases phase)
        {
            if ( IsRegionPage(page) )
            {
                IList<Region> regions = Region.GetRegions(page);
                if (regions.Count > 0)
                {
                    // Check if last component really belongs to the last region, if not let some region gravity happen on it
                    // This is primarily to support adding components on-the-fly in regions via XPM
                    //
                    Region lastRegion = regions[regions.Count-1];
                    if ( lastRegion.ComponentPresentations.Count > 0 )
                    {
                        ComponentPresentationInfo lastCP = lastRegion.ComponentPresentations[lastRegion.ComponentPresentations.Count - 1];
                        if (lastRegion.CanContain(lastCP.ComponentPresentation) == false)
                        {
                            Region region = GetMatchingRegion(regions, lastCP.ComponentPresentation);
                            /*
                            if (region == null)
                            {
                                throw new Exception("No matching region could be found for component: " + lastCP.Component.Title);
                            }
                            */
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
        
    }
}
