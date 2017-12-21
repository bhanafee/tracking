using Microsoft.AspNetCore.Mvc;
using weather.Models;

namespace weather.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController : Controller
    {
        public WeatherController(IWeatherSource source)
        {
            Source = source;
        }
        
        private IWeatherSource Source { get; }

        [HttpGet("{zip}")]
        public string Get(int zip)
        {
            var report = Source.fetch(zip);
            return report.Summary;
        }
    }
}