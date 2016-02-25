#include <stdio.h>
#include "socket.h"

int main(int argc, char *argv[]) {
	//warning: deprecated conversion from string constant to 'char*' [-Wwrite-strings]
	//get("182.92.190.64", 8010, "guoer-appserv/index?device=1&version=3000");
	char* html = getHtml("182.92.190.64", 8010, "guoer-appserv/profile?userId=28&device=1&version=3000");
	printf("\n----%s", html);
	printf("%d", strlen(html));
	return 0;
}
