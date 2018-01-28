# RoboticSimulatorController

# Description:
This is the Controller App, used to control a Robotic Arm in a V-REP simulation using Bluetooth.
The App only works on devices that support blueetooth. 
The min SDK Version is 16

/dagger contains all the dagger relevant classes for dependency injection using Dagger2 <br>
/main contains the MainActivity which is the entry screen of the application and responsible for the Bluetooth handling. <br>
/controller contains the ControllerActivity which acts as the apps main functional screen offering inputs and listneing to sensor montion to control the Robotic Arm. Data send back from the simulator is also visualized in the ControllerActivity <br>

# Installation
The app is ready to install as soon as the gradle sync completes.
The java bluetooth server needs to run on a computer with working bluetooth and can be found here: https://github.com/paatz04/RoboticSimulatorServer
The same computer needs to run the V-REP education simulator with the simulation on it.

# Contributing
All classes should be developed in Kotlin.
Model View Presenter Pattern should be used consistently. 
The views should be dumb and passive, and not contain any business logic.
Material Design Guidelines for design references: https://material.io/guidelines/


# Credits
Patric Corletto
Hannes Oberprantacher
Jose Acevedo

Sebastian Eckl

# Wiki
https://github.com/paatz04/RoboticSimulatorController/wiki
