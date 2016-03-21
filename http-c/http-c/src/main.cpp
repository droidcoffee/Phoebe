//#include <stdio.h>
//#include <string>
//#include <iostream>
//#include <json/json.h>
//#include "socket.h"
//#include "bean/JSON.h"
//#include "bean/UserBean.h"
//
//using namespace std;
//
//int main(int argc, char *argv[]) {
//
//	//warning: deprecated conversion from string constant to 'char*' [-Wwrite-strings]
////	//get("182.92.190.64", 80, "guoer-appserv/index?device=1&version=3000");
//	char* html = getHtml("182.92.190.64", 80, "guoer-appserv/profile?userId=28&device=1&version=3000");
////	printf("\n----%s\n", html);
//
//	JSON json;
//
//	cout << json.getMessage() << "\t" << json.getStatus() << endl;
//
//	Json::Reader reader;
//	Json::Value value;
//	string htmlStr(html);
//	if (reader.parse(htmlStr, value)) {
//		string message = value["message"].asString();
//		string status = value["status"].asString();
//		cout << status << "\t" << message << endl;
//		json.setMessage(message);
//		const Json::Value data = value["result"];
//		int userId = data["id"].asInt();
//		string name = data["name"].asString();
//		string phone = data["phone"].asString();
//		int shbUserId = data["shbUserId"].asInt();
//		UserBean user;
//
////		for (int i = 0; i < arrayObj.size(); i++) {
////			out = arrayObj[i]["key2"].asString();
////			std::cout << out;
////			if (i != arrayObj.size() – 1){
////				std::cout << std::endl;
////			}
////		}
//	}
//	return 0;
//}
