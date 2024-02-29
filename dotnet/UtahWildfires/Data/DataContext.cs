using Microsoft.EntityFrameworkCore;
using UtahWildfires.Models; 

namespace UtahWildfires.Data
{
    public class DataContext : DbContext, IDataContext
    {
        public DataContext(DbContextOptions<DataContext> options): base(options)
        {

        }

        public DbSet<Wildfire> Wildfires {get; set;}
        

    }
}
