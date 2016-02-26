#ifndef JSON_CPP
#define JSON_CPP

#include <string>
using namespace std;

class JSON;

//抽象的父类
class Data {
private:
	JSON * json;
};

class JSON {
private:
	string message;
	int status;
	Data * result;
public:
	void setMessage(string message) {
		this->message = message;
	}
	void setStatus(const int status) {
		this->status = status;
	}
	void setResult(Data *result) {
		this->result = result;
	}

	string getMessage() {
		return this->message;
	}
	const int getStatus() {
		return this->status;
	}

	Data * getResult() {
		return this->result;
	}
};

#endif
