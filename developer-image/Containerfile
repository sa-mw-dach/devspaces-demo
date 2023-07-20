FROM quay.io/devfile/universal-developer-image:latest

# Some examples:
# quay.io/devfile/base-developer-image:ubi9-latest
# quay.io/devfile/universal-developer-image:latest
# quay.io/devfile/universal-developer-image:ubi8-latest
# Check out this repos for options:
# quay.io/repository/devfile/universal-developer-image?tab=tags
# quay.io/repository/devfile/base-developer-image?tab=tags
# https://github.com/devfile/developer-images

RUN curl -fsSL "https://get.sdkman.io" | bash \
    && bash -c ". /home/user/.sdkman/bin/sdkman-init.sh \
    && sed -i "s/sdkman_auto_answer=false/sdkman_auto_answer=true/g" /home/user/.sdkman/etc/config \
    && sed -i "s/sdkman_auto_env=false/sdkman_auto_env=true/g" /home/user/.sdkman/etc/config \
    && sdk install quarkus"
