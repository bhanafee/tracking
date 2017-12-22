﻿using System;

namespace weather.Models
{
    public class OpenWeatherMap : Weather
    {
        public OpenWeatherMap(Endpoint endpoint) : base (endpoint)
        {
        }

        public override IReport ByZip(int zip)
        {
            var raw = Fetcher(new Uri(string.Format(Source.Url, Source.Identifier, Source.Key, zip)));
            return new Report(raw.Result);
        }

        private class Report : IReport
        {
            public Report(string summary)
            {
                Summary = summary;
            } 
            public string Summary { get; }
        }
    }
}