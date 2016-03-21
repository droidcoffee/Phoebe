#include <iostream>
#include <fstream>
#include <string>
using namespace std;

int main(int argc, char **argv) {
	string fileName = "E:/git/Phoebe/http-c/http-c/src/file/file.txt";
	ofstream ofs(fileName.c_str(), fstream::out);
	if (ofs.is_open() == false) {
		cout << "ofs文件打开失败" << endl;
		return -1;
	} else {
		ofs << "hello world" << endl;
		ofs << "hello world 2";
		ofs.close();
	}

	ifstream ifs(fileName.c_str());
	if (ifs.is_open() == false) {
		cout << "文件打开失败" << endl;
		return -1;
	} else {
		char data[256];
		cout << "文件打开成功" << endl;
		while (!ifs.eof()) {
			ifs.getline(data, 256);
			cout << data << " - " << endl;
		}
		ifs.close();
		return 0;
	}
}
