/*
 * SeqList.c
 * 线性表的相关操作
 *  Created on: 2016-1-20
 *      Author: coffee
 */

#include <stdio.h>

#define ListSize 6
typedef int DataType;

struct SeqList {
	DataType data[ListSize]; //数据data用来存放结点
	int length;	// 线性表的当前长度
};

/**
 * 插入元素
 *
 * @param positon 指定的位置 , 从0开始
 */
void insertList(struct SeqList * L, int position, DataType data) {
	//当 position == L->length + 1的时候表示在最后一个地方插入
	if (position < 0 || position > L->length) {
		printf("%d \t %s\n", position, "位置非法");
		return;
	}
	if (L->length >= ListSize) {
		printf("%s\n", "列表已满");
		return;
	}
	int j;
	for (j = L->length; j > position; j--) {
		L->data[j] = L->data[j - 1];
	}
	L->data[position] = data;
	L->length++;
}

/**
 * 删除SeqList的指定位置position的元素<br>
 * @param position 从0开始
 */
DataType deletelist(struct SeqList * L, int position) {
	if (position < 0 || position > L->length - 1) {
		printf("%d \t %s\n", position, "位置非法");
		return -1;
	}
	DataType x = L->data[position];
	//如果删除的是最后一个问题
	if (position == L->length) {
		L->length--;
	} else {
		int i;
		for (i = position; i < L->length; i++) {
			L->data[i] = L->data[i + 1];
		}
		L->length--;
	}
	printf("已删除第%d个位置的元素%d \n", position, x);
	return x;
}

/**
 * 打印SeqList的所有元素
 */
void printSeqList(struct SeqList * L) {
	int i = 0;
	for (i = 0; i < L->length; i++) {
		printf("%d ", L->data[i]);
	}
	printf("\n");
}
/**
 * 测试插入元素
 */
void testInsertSeqList() {
	struct SeqList s = { .length = 5, .data = { 1, 3, 5, 7, 9 } };
	insertList(&s, -1, 2);	//位置非法
	insertList(&s, 6, 2);	//位置非法
	printSeqList(&s);
	insertList(&s, 0, 2);	//0位置插入元素2
	insertList(&s, 1, 11);	//1位置插入元素2
	insertList(&s, 5, 55);	//1位置插入元素2

	printSeqList(&s);
}

/**
 * 测试删除元素
 */
void testDeleteSeqList() {
	struct SeqList s = { .length = 5, .data = { 1, 3, 5, 7, 9 } };
	deletelist(&s, -1);
	deletelist(&s, 6);
	deletelist(&s, 3);
	printSeqList(&s);
}

int main1() {
	//testInsertSeqList();
	testDeleteSeqList();
	return 0;
}

