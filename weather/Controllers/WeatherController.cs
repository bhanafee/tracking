using Microsoft.AspNetCore.Mvc;
using weather.Models;

namespace weather.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController : Controller
    {
        public WeatherController(Weather source)
        {
            Source = source;
        }
        
        private Weather Source { get; }

        [HttpGet("{zip}")]
        public string Get(int zip)
        {
            var report = Source.ByZip(zip);
            return report.Summary;
        }
    }
}