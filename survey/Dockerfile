FROM tutum/nginx
MAINTAINER sven.lenz@msc.htwchur.ch

RUN rm /etc/nginx/sites-enabled/default
ADD sites-enabled/ /etc/nginx/sites-enabled
ADD app/ /src/app