using Microsoft.EntityFrameworkCore;
using UtahWildfires.Models; 

namespace UtahWildfires.Data
{
    public interface IDataContext
    {
        public DbSet<Wildfire> Wildfires {get; set;}
        
    }
}
