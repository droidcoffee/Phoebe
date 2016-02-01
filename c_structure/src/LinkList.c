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

void printLinkListNoHead(struct LinkNode * node);

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

/**
 * 后插-带头指针
 */
struct LinkNode * createListR1() {
	struct LinkNode * head = (struct LinkNode *) malloc(sizeof(struct LinkNode));
	struct LinkNode * rear = head;
	struct LinkNode * p = NULL;
	char ch = '0';
	printf("%s", "创建LinkList, 按回车键结束输入\n");
	while ((ch = getchar()) != '\n') {
		p = (struct LinkNode *) malloc(sizeof(struct LinkNode));
		p->data = ch;
		rear->next = p;
		rear = p;
	}
	printf("%s", "创建结束\n");
	rear->next = NULL;
	return head;
}
/**
 *
 * @param position 从1开始, 其中0是head节点
 */
struct LinkNode * getNodeByPosition(struct LinkNode * head, int position) {
	if (position < 1) {
		printf("位置非法%d", position);
		return NULL;
	}
	int i = 0;
	struct LinkNode * p = head->next;
	while (p != NULL) {
		i++;
		if (position == i) {
			return p;
		}
		p = p->next;
	}
	return NULL;
}
/**
 * 查找给定值得结点
 */
struct LinkNode * getNodeByValue(struct LinkNode * head, DataType data) {
	struct LinkNode * p = head;
	while (p->next != NULL) {
		p = p->next;
		if (p->data == data) {
			return p;
		}
	}
	return NULL;
}

/**
 * 在指定的位置插入一个新结点，结点值为data<br>
 * position 从1开始.当为1的时候表示在head结点之后插入，也就是第一个结点
 */
void insertLinkList(struct LinkNode * head, int position, DataType data) {
	if (position < 1) {
		printf("插入位置非法 %d \n", position);
		return;
	}
	struct LinkNode * node = head;
	int i = 0;
	//先查找position的前一个结点 (注意position的前一个元素，从0开始找,即头结点处开始找)
	while (node != NULL) {
		if (i == position - 1) {
			break;
		}
		node = node->next;
		i++;
	}

	if (node == NULL) {
		printf("插入失败, position的前一个结点 == NULL\n");
	} else {
		struct LinkNode * p = (struct LinkNode *) malloc(sizeof(struct LinkNode));
		struct LinkNode * tmp = node->next; //node的下一个结点
		node->next = p;
		p->next = tmp;
		p->data = data;
	}
}
/**
 * 位置从1开始
 */
void deleteLinkNode(struct LinkNode * head, int position) {
	struct LinkNode * p = head; // p指向position的前一个结点
	int i = 0;
	while (p != NULL) { // qw 3
		if (i == position - 1) {
			break;
		}
		p = p->next;
		i++;
	}
	// p的前一个结点不存在， 或者p是最后一个结点，都讲导致删除失败
	if (p == NULL || p->next == NULL) {
		printf("位置错误, 删除第%d个元素失败\n", position);
	} else {
		// 记录要删除的前一个元素
		struct LinkNode * s = p;
		p = p->next; //移动要要删除的元素
		//删除的是最后一个元素
		if (p == NULL) {
			s->next = NULL;
		} else {
			s->next = p->next;
		}
	}
}

/**
 * 遍历--带头结点
 */
void printLinkList(struct LinkNode * head) {
	if (head == NULL) {
		printf("遍历---LInkList == NULL \n");
		return;
	}
	//第一个结点
	struct LinkNode * node = head->next;
	printLinkListNoHead(node);
}

/**
 * 遍历LinkList不带头结点
 */
void printLinkListNoHead(struct LinkNode * node) {
	printf("开始遍历LinkList\n");
	while (node != NULL) {
		printf("%c-", node->data);
		node = node->next;
	}
	printf("遍历结束\n");
}

int main() {
// struct LinkNode * head = createkListF();
// struct LinkNode * head = createListR();
	struct LinkNode * head = createListR1();

	printLinkList(head);
//
//	struct LinkNode * secondNode = getNodeByPosition(head, 2);
//	printf("查找第二个元素 -- %c\n", secondNode->data);
//
//	struct LinkNode * eNode = getNodeByValue(head, 'e');
//	printf("查找第二个元素 以及之后的元素\n");
//	printLinkListNohead(eNode);

//	int position = 7;
//	printf("第%d个位置插入元素 'x'\n", position);
//	insertLinkList(head, position, 'x');
//	printLinkList(head);

	int position = 2;
	printf("第%d个位置删除元素\n", position);
	deleteLinkNode(head, position);
	printLinkList(head);
	return 0;
}
