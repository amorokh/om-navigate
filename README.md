# om-navigate

This is an example of using [React Navigation](http://reactnavigation.org) from an [om-next](https://github.com/omcljs/om/wiki/Quick-Start-(om.next)) application.

I make no attempt to incorporate the navigation state into om-next's state handling, I just make it possible to use navigators from om-next.

The example app is a port of the [NavigationPlayground](https://github.com/react-community/react-navigation/tree/master/examples/NavigationPlayground) (currently the only implemented sections are SimpleStack and SimpleTab).

## Run

Requirements:

* node & npm
* leiningen
* re-natal
* react-native-cli

To run:

`npm install`

`re-natal use-figwheel`

`react-native run-ios`

## Solution

The solution is based on the idea of dynamically creating proxy components for navigators and screens using the `ui` macro.

### Navigator proxy

The navigator proxy collects queries from the screens of the route config and returns those query fragments in its own implementation of the IQuery protocol. When the proxy is rendered it passes the om-next props to route screens in the `screenProps` defined by React Navigation. The proxy also renders the actual navigator for which it is a proxy.

### Screen proxy

The screen proxy picks up the om-next props fron the `screenProps` and the [navigation prop](https://reactnavigation.org/docs/navigators/navigation-prop), merges these and passes the result as the om-next props to the actual screen component. 
