:: This batch script helps to do autohibernate which is disabled in Windows 11
:: see https://answers.microsoft.com/en-us/windows/forum/all/windows-11-hibernate-enabled-cannot-auto-hibernate/4d2c4fde-c682-4ecd-a3c8-844dfa7c0b07
:: Once this script installed, everytime when Windows goes to sleep it will
:: automatically hibernate.
::
:: Setup:
:: - Make sure that Sleep is activated: System -> Power -> Sleep
:: - Open Task Scheduler
:: - Create Task
::  - Name: winhiber
::  - Run when user is logged on or not: Yes
::  - Triggers -> New -> On an event
::   - Log: System
::   - Source: Kernel Power
::   - Event ID: 506
::  - Actions -> Start a program
::   - Select this batch script
::  - Settings
::   - Do not start a new instance
::
:: Once hibernate is requested this tasks sleeps for 3min.
:: It is needed to avoid infinite hibernation loop (when Windows
:: wakes up it usually triggers the event again
:: which triggers this script over and over). Adding sleep prevents Windows
:: from starting yet another task right after hibernate. Unfortunatelly
:: it has one side effect is that when you shutdown Windows then on
:: next startup it will immediately hibernate. In this case you need to
:: resume it back and it will start normally.
::
:: Copyright 2022 lambdaprime
:: 
:: Website: https://github.com/lambdaprime
::

shutdown /h
timeout 180
