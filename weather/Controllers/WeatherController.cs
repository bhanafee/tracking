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

        [HttpGet]
        public string Coordinates([FromQuery] float lon, [FromQuery]float lat)
        {
            var report = Service.ByCoordinates(lon, lat);
            return report.Summary;
        }
        
        [HttpGet("{zip}")]
        public string Zip(int zip)
        {
            var report = Service.ByZip(zip);
            return report.Summary;
        }
    }
}