using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using UtahWildfires.Models;

namespace UtahWildfires.Repositories
{
    public interface IWildfireRepository
    {
        Task<IEnumerable<Wildfire>> GetAllWildfires();

        Task<IEnumerable<Wildfire>> GetTenLargestWildfires();

        Task<IEnumerable<Wildfire>> GetLargestWildfire();

        Task<IEnumerable<Wildfire>> GetSmallestWildfire();

        Task<IEnumerable<Wildfire>> GetWildfiresByYear(DateTime year);
        
    }
}