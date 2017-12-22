using Microsoft.AspNetCore.Mvc;
using weather.Models;

namespace weather.Controllers
{
    [Route("api/[controller]")]
    public class WeatherController : Controller
    {
        public WeatherController(Weather service)
        {
            Service = service;
        }
        
        private Weather Service { get; }

        [HttpGet("{zip}")]
        public string Get(int zip)
        {
            var report = Service.ByZip(zip);
            return report.Summary;
        }
    }
}