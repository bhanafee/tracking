using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;

namespace weather.Models
{
    public class OpenWeatherMap : Weather
    {
        public OpenWeatherMap(Endpoint endpoint) : base (endpoint)
        {
        }

        public override IReport ByZip(int zip)
        {
            var builder = BuildUri();
            // Don't worry about empty query string, because OpenWeatherMap requires APPID parameter in the base URI.
            builder.Query = builder.Query.Substring(1) + string.Format("&zip={0:00000},us", zip);
            var serializer = new DataContractJsonSerializer(typeof(Report));
            var stream = Fetch(builder.Uri).Result;
            return serializer.ReadObject(stream) as Report;
        }

        public override IReport ByCoordinates(float lon, float lat)
        {
            var builder = BuildUri();
            // Don't worry about empty query string, because OpenWeatherMap requires APPID parameter in the base URI.
            builder.Query = builder.Query.Substring(1) + string.Format("&lat={0}&lon={1}", lat, lon);
            var serializer = new DataContractJsonSerializer(typeof(Report));
            var stream = Fetch(builder.Uri).Result;
            return serializer.ReadObject(stream) as Report;
        }

        [DataContract]
        public class Report : IReport
        {
            [DataMember(Name="weather")]
            public List<Condition> Conditions { get; set; }
            
            [DataMember(Name="name")]
            public string City { get; set; }
            
            public string Summary => String.Format("In {0} the weather is {1}.", City, Conditions.First().Description);
        }

        [DataContract]
        public class Condition
        {
            [DataMember(Name="main")]
            public string Main { get; set; }
            
            [DataMember(Name="description")]
            public string Description { get; set; }
        }
    }
}