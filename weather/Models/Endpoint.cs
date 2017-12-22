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
}