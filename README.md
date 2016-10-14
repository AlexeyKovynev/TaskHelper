# TaskHelper
App allows to get processes, group them by name, export XML and XLSX (with chart generating), import saved XML to compare processes with current ones

You should have JRE 8 installed on your PC to run this program.
Application written using JavaFX 2 and has following functions:
- It runs "tasklist" as a console command;
- The output of the command returned to the application and shows up the following information within the graphical user interface: Name, Process ID, Used Memory;
- The application provides a manner of handling different OS language settings.
- Has option to remove any name duplicates from the listed task (ignoring the PID).
- Tasks of the same name being grouped together and the used memory aggregated.
- Grouped list of tasks can be exported into XML file.
- Saved XML files can be re-imported into app. After importing an XML user can compare <br>the contents to the currently running system tasks. Any changes are displayed in the GUI.
- Gathered data can be exported into Microsoft Excel with generating a chart about the memory usage of tasks.
