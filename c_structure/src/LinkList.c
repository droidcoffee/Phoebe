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

/**
 * 建立LinkList <br>
 * head插入法<br>
 */
struct LinkNode * createkListF() {
	struct LinkNode * head = NULL; //注意初始化一定要赋值为NULL
	struct LinkNode * p = NULL; //定义指向结点的指针变量
	char ch;
	printf("%s", "初始化LinkList, 按回车键结束输入\n");
	while ((ch = getchar()) != '\n') {
		p = (struct LinkNode *) malloc(sizeof(struct LinkNode));
		p->data = ch;
		p->next = head; //指向头指针
		head = p; //重置头指针
	}
	return head;
}
/**
 * 建立LinkList <br>
 * 尾部插入法  与头部插入法区别在于“尾插法”多了一个Head结点<br>
 */
struct LinkNode * createListR() {
	struct LinkNode * head = NULL;
	struct LinkNode * rear = NULL;
	struct LinkNode * p = NULL;

	char ch;
	printf("%s", "初始化LinkList, 按回车键结束输入\n");
	while ((ch = getchar()) != '\n') {
		p = (struct LinkNode *) malloc(sizeof(struct LinkNode));
		p->data = ch;
		p->next = NULL;  //****注意该节点一定要初始化
		if (head == NULL) { // head结点只需要赋值一次
			head = p;
		} else {
			rear->next = p;
		}
		rear = p; // 尾结点, 总是指向最后的节点
	}
	return head;
}

void printLinkList(struct LinkNode * head) {
	if (head == NULL) {
		printf("遍历---LInkList == NULL \n");
		return;
	}
	struct LinkNode * node = head;
	printf("开始遍历LinkList\n");
	while (node != NULL) {
		printf("%c ", node->data);
		node = node->next;
	}
	printf("遍历结束\n");
}

int main() {
	// struct LinkNode * head = createkListF();
	struct LinkNode * head = createListR();
	// printf("%c\n", head->data);
	printLinkList(head);
	return 0;
}
