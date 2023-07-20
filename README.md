# devspaces-demo Project

## Prerequisites

All deployment objects, prerequsites included, are stored in the `deployment` folder.

All objects required for building a custom image are stored in the `developer-image` folder.

### Needed Operators:
* OpenShift Dev Spaces
* OpenShift Pipelines
* OpenShift GitOps

### Needed Workspaces:

This project uses a custom created Universal Developer Image, which is provided in the `developer-image` folder.

The whole topology consists of 3 namespaces, the devspaces namespace containing the OpenShift Dev Spaces (CheCluster) instance, the devspaces-image namespace containing the quarkus developer image pipeline and the devspaces-demo namespace containing this project.

* Create Namespaces
  ```sh
  oc new-project devspaces
  oc new-project devspaces-image
  oc new-project devspaces-demo
  ```
* Annotate Namespaces for OpenShift GitOps
  ```
  oc annotate namespace devspaces-demo argocd.argoproj.io/managed-by=openshift-gitops
  oc annotate namespace devspaces-image argocd.argoproj.io/managed-by=openshift-gitops
  ```
* set policy to OpenShift gitOps
  ```
  oc adm policy add-role-to-user admin system:serviceaccount:openshift-gitops:openshift-gitops-argocd-application-controller -n devspaces-demo
  oc adm policy add-role-to-user admin system:serviceaccount:openshift-gitops:openshift-gitops-argocd-application-controller -n devspaces-image
  ```

### Needed Credentials:
The pipeline of this project pushes its image to quay.io. Dev Spaces needs git credentials to commit to the repository.
* Quay.io username and cli token
* GitHub user id and secret
* Have a look at the `deployment/2-github-and-quay-secrets.yaml` file, insert your credentials and apply this file
  * [further information regarding the github oAuth](https://access.redhat.com/documentation/en-us/red_hat_openshift_dev_spaces/3.0/html-single/administration_guide/index#configuring-oauth-2-for-github)
* Link quay secret to pipeline ServiceAccount
  ```sh
  oc secret link pipeline quay-secret
  ```

### Further interesting stuff:
Depending on your usage you will need to share (in kubernetes that means re-create) a secret in multiple namespaces.
* sharing a secret (for example the quay-secret, just change the name to github-oauth-config for the git secret) between namespaces via copy&paste:
  ```sh
  oc get secret quay-secret -o json \
  | jq 'del(.metadata["namespace","creationTimestamp","resourceVersion","selfLink","uid"])' \
  | oc apply -n devspaces-demo -f -
  ```

## Dev Spaces Demo

This dev spaces demo shows a full circle for a workspace environment with connected pipelines and gitops. To deploy (and after you have completed the prerequisites), configure the `argoapp.yaml` file and apply it in your namespace via:
```sh
oc apply -f deployment/argoapp.yaml
```

The main interesting part of this repository is the `devfile.yaml` containing all the dev spaces configuration of the specific namespace.

The pipeline and deployment is accomplished via helm. Have a look at the `deployment/helm folder` for futher details.

### Use this project on your own

To use this project on your own you'll have to do these steps:
1. Fork this project
2. Create the devspaces-demo repository in quay.io
3. Set your git and quay repo urls in the `deployment/helm/values.yaml` file
4. Set your git repo url in the `deployment/argoapp.yaml` file
5. Commit everything
6. Set your github and quay.io credentials in the `deployment/2-github-and-quay-secrets.yaml` file and apply it to your cluster in your devspaces-demo namespace
7. Apply the argo app via `oc apply -f deployment/argoapp.yaml`
8. In the browser, navigate in your forked github devspaces-demo project to the settings page and open your webhooks
9. Copy your pipeline event listener url via `oc get route devspaces-demo-el-route -n devspaces-demo -o go-template='{{.spec.host}}'`
10. Create a webhook, paste the pipeline and set the content type to json
11. Open your Dev Spaces project, for example via link: [https://devspaces.apps.ocp.ocp-gm.de/#https://github.com/gmodzelewski/devspaces-demo]
12. Start coding cool stuff

If you want to create your own custom images, have a look at the developer-image folder. Complete the steps documented in the `developer-image/README.md` there and set the name of your quay.io developer image repo in the `devfile.yaml`.
