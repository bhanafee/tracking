using System;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

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

        public Report ByZip(int zip)
        {
            var raw = Fetcher(zip);
            return new Report(zip, raw.Result);
        }

        private async Task<string> Fetcher(int zip)
        {
            var client = new HttpClient(); 
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(
                new MediaTypeWithQualityHeaderValue("text/html"));
            var uri = new Uri(string.Format(Endpoint.Url, Endpoint.Identifier, Endpoint.Key, zip));
            return await client.GetStringAsync(uri);
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