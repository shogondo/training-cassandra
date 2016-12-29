using System;
using Cassandra;

class Program
{
	private Cluster cluster;

	private ISession session;

	static void Main(string[] args)
	{
		var program = new Program();
		program.Start();
	}

	private void Start()
	{
		Connect();
		Find("JP001");
		Search();
	}

	private void Connect()
	{
		Console.WriteLine("Connecting...");
		cluster = Cluster.Builder().AddContactPoint("192.168.33.10").Build();
		session = cluster.Connect("hotel");
	}

	private void Search()
	{
		Console.WriteLine("Searching rows...");
		var rows = session.Execute("SELECT * FROM hotels");
		Print(rows);
	}

	private void Find(string id)
	{
		Console.WriteLine("Finding row (id={0})...", id);
		var statement = session.Prepare("SELECT * FROM hotels WHERE id = ?");
		var rows = session.Execute(statement.Bind(id));
		Print(rows);
	}

	private void Print(RowSet rows)
	{
		foreach (var row in rows)
		{
			Console.WriteLine(
				"id: {0}, name: {1}, phone: {2}",
				row["id"],
				row["name"],
				row["phone"]);
		}
	}
}
