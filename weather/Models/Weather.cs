using System;

namespace weather.Models
{
    public class Endpoint
    {
        public Endpoint(string url, string identifier, string key)
        {
            if (string.IsNullOrEmpty(url))
            {
                throw new ArgumentException("Endpoint URL missing", nameof(url));
            }
            Url = url;
            Identifier = identifier;
            Key = key;
        }

        public string Url { get;  }
        public string Identifier { get; }
        public string Key { get; }
    }

    public class Weather
    {
        public Weather(Endpoint endpoint)
        {
            Endpoint = endpoint;
        }
        
        private Endpoint Endpoint { get; }

        public Report Fetch(int zip)
        {
            var summary = string.Format("For {0:d5} the weather is sunny", zip);
            return new Report(zip, summary);
        }

        public class Report 
        {
            public Report(int zip, string summary)
            {
                Zip = zip;
                Summary = summary;
            }

            public int Zip { get; }

            public string Summary { get; }
        }
    }
}