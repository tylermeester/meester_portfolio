using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using UtahWildfires.Models;
using UtahWildfires.Data;
using Microsoft.EntityFrameworkCore;

namespace UtahWildfires.Repositories
{
    public class WildfireRepository : IWildfireRepository
    {

        private readonly IDataContext _context;

        public WildfireRepository(IDataContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Wildfire>> GetAllWildfires()
        {
            return await _context.Wildfires.ToListAsync();
        }

        public async Task<IEnumerable<Wildfire>> GetTenLargestWildfires()
        {
            var q = _context.Wildfires
                .OrderByDescending(wildfire => wildfire.Acres)
                .Take(10)
                .ToListAsync();
            
            return await q;
        }


        public async Task<IEnumerable<Wildfire>> GetLargestWildfire()
        {
            var q = _context.Wildfires
                .OrderByDescending(wildfire => wildfire.Acres)
                .Take(1)
                .ToListAsync();
            
            return await q;
        }

        public async Task<IEnumerable<Wildfire>> GetSmallestWildfire()
        {
            var q = _context.Wildfires
                .OrderBy(wildfire => wildfire.Acres)
                .Take(1)
                .ToListAsync();
            
            return await q;
        }

        public async Task<IEnumerable<Wildfire>> GetWildfiresByYear(DateTime year)
        {
            return await _context.Wildfires
                .Where(w => w.Year.HasValue && w.Year.Value.Year == year.Year)
                .ToListAsync();
        }


        
    }
}