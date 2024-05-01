part of 'authentication_bloc.dart';

enum AuthenticationStatus{unauthenticated, authenticated, unknown}

class AuthenticationState extends Equatable{
 final AuthenticationStatus status;
 final MyUser? user;

 const AuthenticationState._({
  this.status = AuthenticationStatus.unknown,
  this.user
});

 const AuthenticationState.unknown(): this._();
 const AuthenticationState.unauthenticated(): this._(status: AuthenticationStatus.unauthenticated);
 const AuthenticationState.authenticated(MyUser user): this._(status: AuthenticationStatus.unauthenticated, user: user);

 @override
 List<Object?> get props => [status, user];
}