service ExternerClientService{
	list<string>historieAbfragen(i32 s),
	string statusAbfragen(),
	string getStatusAnderung(),
	string getLeistungsAnderung(),
	void historieSetzen(list<string> a),
	list<string> historieBekommen(i32 min, i32 max)
}
