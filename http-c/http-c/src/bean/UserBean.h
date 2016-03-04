/*
 * UserBean.h
 *
 *  Created on: 2016-3-1
 *      Author: DELL
 */

#ifndef SRC_USERBEAN_H_
#define SRC_USERBEAN_H_
#include <iostream>
using namespace std;
class UserBean: Data {
private:
	int id;
	string name;
	string phone;
	int shbUserId;
public:
	UserBean() {
		id = 0;
		name = "";
		phone = "";
		shbUserId = 0;
	}
	UserBean(int id, string name, string phone, int shbUserId) {
		this->id = id;
		this->name = name;
		this->phone = phone;
		this->shbUserId = shbUserId;
	}
	int getId() const {
		return id;
	}

	void setId(int id) {
		this->id = id;
	}

	const string& getName() const {
		return name;
	}

	void setName(const string& name) {
		this->name = name;
	}

	const string& getPhone() const {
		return phone;
	}

	void setPhone(const string& phone) {
		this->phone = phone;
	}

	int getShbUserId() const {
		return shbUserId;
	}

	void setShbUserId(int shbUserId) {
		this->shbUserId = shbUserId;
	}
};

#endif /* SRC_USERBEAN_H_ */
