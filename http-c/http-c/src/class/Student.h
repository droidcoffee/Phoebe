/*
 * Student.h
 *
 *  Created on: 2016-3-10
 *      Author: DELL
 */

#ifndef SRC_CLASS_STUDENT_H_
#define SRC_CLASS_STUDENT_H_
#include <iostream>
#include <string.h>
using namespace std;
class Student {
private:
	string name;

public:
	Student();
	virtual ~Student();
	virtual string getName() {
		return name;
	}
	virtual void setName();
};

#endif /* SRC_CLASS_STUDENT_H_ */
