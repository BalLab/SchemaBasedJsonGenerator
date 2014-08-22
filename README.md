SchemaBasedJsonGenerator
========================

Simplistic Schema Based JSON Data Generator.

A quick hack to generate random json data for work load testing. Uses the wonderful DataFactory and LatLng libraries. A Sample schema is provided in the directory src/main/resources. 

Please note that this is a quick hack and requires lots of improvement. Hacked it out since there really is nothing even close to this on the net :-(.

It is fine for using as a base for quick and dirty work. Will be improved in the coming months.

Usage Example:

JsonGen app = new JsonGen(filePath);
		
	for(int i= 0;i < repeat; i++){
		System.out.println(app.generate());
	}


# Schema Sample
{
  "integer":"getInt(10,20)",
  "email":"getEmail()",
  "latitude":"getLatitude()",
  "longitude":"getLongitude()",
  "name": "getName()",
  "list": "getFromList(cat,mouse,dog,horse)",
  "double":"getDouble(10,20)"
}
