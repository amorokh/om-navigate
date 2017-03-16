om_navigate.navigation.DelegatingRouter.prototype.getStateForAction = (function() {
    var G__42175 = null;

    var G__42175__0 = (function (){
        var self__ = this;
        var action = this;
        return self__.router.getStateForAction(action);
    });

    var G__42175__1 = (function (inputState){
        var self__ = this;
        var action = this;
        return self__.router.getStateForAction(action,inputState);
    });

    var G__42175__2 = (function (inputState,t){
        var self__ = this;
        var action = this;
        return self__.router.getStateForAction(action,inputState,t);
    });

    G__42175 = function(inputState,t){
        switch(arguments.length){
            case 0:
                return G__42175__0.call(this);
            case 1:
                return G__42175__1.call(this,inputState);
            case 2:
                return G__42175__2.call(this,inputState,t);
        }
        throw(new Error('Invalid arity: ' + arguments.length));
    };
    G__42175.cljs$core$IFn$_invoke$arity$0 = G__42175__0;
    G__42175.cljs$core$IFn$_invoke$arity$1 = G__42175__1;
    G__42175.cljs$core$IFn$_invoke$arity$2 = G__42175__2;
    return G__42175;
})();

om_navigate.navigation.DelegatingRouter.prototype.getStateForAction = (function() {
    var G__42421 = null;
    
    var G__42421__1 = (function (action){
        var self__ = this;
        var this$ = this;
        return self__.router.getStateForAction(action);
    });

    var G__42421__2 = (function (action,inputState){
        var self__ = this;
        var this$ = this;
        return self__.router.getStateForAction(action,inputState);
    });

    G__42421 = function(action,inputState){
    switch(arguments.length){
    case 1:
    return G__42421__1.call(this,action);
    case 2:
    return G__42421__2.call(this,action,inputState);
    }
    throw(new Error('Invalid arity: ' + arguments.length));
    };
    G__42421.cljs$core$IFn$_invoke$arity$1 = G__42421__1;
    G__42421.cljs$core$IFn$_invoke$arity$2 = G__42421__2;
    return G__42421;
})();
