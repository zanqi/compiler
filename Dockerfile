FROM --platform=linux/amd64 ubuntu:latest
ENV DEBIAN_FRONTEND=noninteractive

RUN sed -i 's/archive.ubuntu.com/mirrors.tuna.tsinghua.edu.cn/g' \
    /etc/apt/sources.list
RUN dpkg --add-architecture i386

RUN apt-get update
RUN apt-get install -y ca-certificates
RUN apt-get install -y flex bison build-essential csh
# RUN apt-get install -y libc6:i386 libxaw7-dev
RUN apt-get install -y libc6:i386 libxaw7:i386

# WORKDIR /usr/class/cs143/cool
# COPY --chown=root:root . .

# WORKDIR /root/cs143

ENTRYPOINT ["/bin/bash"]