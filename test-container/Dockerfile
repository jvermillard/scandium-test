FROM jvermillard/luadtls
MAINTAINER jvermillard@gmail.com

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update

RUN apt-get install -y git openjdk-7-jdk
RUN apt-get install -y wget

# maven 3.2.2
RUN cd /tmp && wget http://apache.org/dist/maven/maven-3/3.2.2/binaries/apache-maven-3.2.2-bin.tar.gz
RUN cd /opt && tar xzf /tmp/apache-maven-3.2.2-bin.tar.gz && ln -fs /opt/apache-maven-3.2.2/bin/mvn /usr/bin/mvn

ADD run-test.sh /build/run-test.sh

WORKDIR /build
ENTRYPOINT ["/bin/bash" , "./run-test.sh"]
