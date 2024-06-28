# FROM  fluent/fluentd-kubernetes-daemonset:v1.16-debian-elasticsearch7-1
FROM  fluent/fluentd-kubernetes-daemonset:v1.4.2-debian-elasticsearch-1.1

# due to limited support in the Fluentd plugin for Elasticsearch, use version 7.
# see https://github.com/uken/fluent-plugin-elasticsearch/issues/1005

RUN gem install fluent-plugin-detect-exceptions -v 0.0.12 \
 && gem sources --clear-all \
 && rm -rf /var/lib/apt/lists/* \
           /home/fluent/.gem/ruby/2.3.0/cache/*.gem
