#include <stdio.h>
#include <string>
#include <iostream>
#include <json/json.h>
#include "JSON.h"

using namespace std;

int main(int argc, char *argv[]) {
	//warning: deprecated conversion from string constant to 'char*' [-Wwrite-strings]
	//get("182.92.190.64", 8010, "guoer-appserv/index?device=1&version=3000");
//	char* html = getHtml("182.92.190.64", 8010, "guoer-appserv/profile?userId=28&device=1&version=3000");
//	printf("\n----%s\n", html);
//	printf("%d", strlen(html));
	//char * content = "{"message":"success","result":{"id":28,"name":"王涛","phone":"15210788660","shbUserId":847383},"status":"1"}";

	printf("hello world");

	JSON json;
	string message = "hello 12434";
	json.setMessage(message);

	// printf("%s", json.getMessage());
	cout << json.getMessage() << endl;

	std::string strValue = "{\"key1\":\"value1\",\"array\":[{\"key2\":\"value2\"},{\"key2\":\"value3\"},{\"key2\":\"value4\"}]}";

	Json::Reader reader;
	Json::Value value;

	if (reader.parse(strValue, value)) {
		std::string out = value["key1"].asString();
		std::cout << out << std::endl;
//		const Json::Value arrayObj = value["array"];
//		for (int i = 0; i < arrayObj.size(); i++) {
//			out = arrayObj[i]["key2"].asString();
//			std::cout << out;
//			if (i != arrayObj.size() – 1){
//				std::cout << std::endl;
//			}
//		}
	}
	return 0;
}
