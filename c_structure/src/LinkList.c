/*
 * LinkList.c
 *
 *  Created on: 2016-1-22
 *      Author: coffee
 */

#include <stdio.h>
#include <stdlib.h>

typedef char DataType;

struct LinkNode {
	DataType data;
	struct LinkNode * next;
};

int main() {
	struct LinkNode * head = NULL; // 指向单链表的头指针
	struct LinkNode * p; //定义指向结点的指针变量
	char ch;
	while ((ch = getchar()) != '0') {
		p = (struct LinkNode *) malloc(sizeof(struct LinkNode));
		p->data = ch;
		p->next = head; //指向头指针
		head = p; //重置头指针
	}

	struct LinkNode * node = head;
	while(node != NULL){
		printf("%c ", node->data);
		node = node->next;
	}
	return 0;
}
