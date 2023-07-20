[Contribute via OpenShift Dev Spaces](https://devspaces.apps.ocp.ocp-gm.de/#https://github.com/gmodzelewski/quarkus-developer-image)

[Contribute via OpenShift Dev Spaces via intellij-ce](https://devspaces.apps.ocp.ocp-gm.de/#https://github.com/gmodzelewski/quarkus-developer-image?new&che-editor=che-incubator/che-idea/latest)

# quarkus-developer-image

Used to describe the process of creating custom images in a cicd way for OpenShift Dev Spaces.
Image gets pushed to quay, so a user secret is needed.

## Prerequisites

Assuming you are using this project as a part of the [devspaces-demo project](https://github.com/gmodzelewski/devspaces-demo), you will have the steps described in the Readme there already completed.
You will need to create the quay.io secret in the devspaces-image namespace as well.

```sh
oc project devspaces-image

oc get secret quay-secret -o json \
| jq 'del(.metadata["namespace","creationTimestamp","resourceVersion","selfLink","uid"])' \
| oc apply -n devspaces-demo -f -

oc secret link pipeline quay-secret
```

## Custom (Quarkus Developer) Image

This project shows a full circle for a workspace environment with connected pipelines and gitops. To deploy (and after you have completed the prerequisites), configure the `argoapp.yaml` file and apply it in your namespace via:
```sh
oc apply -f argoapp.yaml
```

### Use this project on your own

To use this project on your own you'll have to do these steps:
1. Fork this project
2. Create the devspaces-image repository in quay.io
3. Set your git and quay repo urls in the `helm/values.yaml` file
4. Set your git repo url in the `argoapp.yaml` file
5. Commit everything
6. Apply the argo app via `oc apply -f argoapp.yaml`
7. In the browser, navigate in your forked github devspaces-image project to the settings page and open your webhooks
8. Copy your pipeline event listener url via `oc get route quarkus-developer-image-el-route -n devspaces-image -o go-template='{{.spec.host}}'`
9.  Create a webhook, paste the pipeline and set the content type to json
10. Open your Dev Spaces project, for example via link: [https://devspaces.apps.ocp.ocp-gm.de/#https://github.com/gmodzelewski/quarkus-developer-image]
11. Start coding cool stuff

### Bugfixing:

#### PVC stuck - delete pvc

The pvcs are configured to be not deleted at helm uninstall. You should always delete them manually.

If some pvc gets stuck you can fix this via
```sh
oc patch pvc <pvc-name> -p '{"metadata":{"finalizers": []}}' --type=merge
```
