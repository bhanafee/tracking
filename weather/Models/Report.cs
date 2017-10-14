namespace weather.Models
{
    public class Report
    {
        private readonly string _content;

        public Report(string content)
        {
            _content = content;
        }

        public string Content => _content;
    }
}