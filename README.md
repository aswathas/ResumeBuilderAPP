# ResumeBuilderAPP

A desktop resume builder written in Java Swing. You fill in a form across a set of
tabs, watch a live text preview, then export the result as a PDF.

There is no web server and no install step — it is a single windowed application
that runs on the machine it is built on.

## What it does

- **Tabbed form** for personal details, professional summary, skills, work
  experience, education, and an optional profile photo
  (`src/main/java/com/resumebuilder/gui/MainWindow.java`).
- **Live preview** panel that updates as you type, driven by a `DocumentListener`
  on the text fields.
- **Download PDF** button in the menu bar — renders the resume to a `.pdf` file
  you pick with a save dialog, using iText 7 (`util/PdfGenerator.java`, A4 page,
  embedded profile image when one is set).
- **Save Resume** button — inserts the resume into a database through a DAO
  (`dao/ResumeDAO.java`).

## Requirements

- JDK 8 or later (the pom compiles with `source`/`target` 1.8)
- Maven 3
- A desktop environment — Swing needs a display; this will not run headless

## Build and run

```bash
mvn compile
mvn exec:java          # main class is com.resumebuilder.gui.MainWindow
```

To get a runnable jar with all dependencies bundled:

```bash
mvn package            # target/resume-builder-1.0-SNAPSHOT.jar
java -jar target/resume-builder-1.0-SNAPSHOT.jar
```

`mvn clean package` for a fresh build.

## Project layout

```
src/main/java/com/resumebuilder/
  gui/MainWindow.java      Swing UI, entry point, live preview
  model/Resume.java        Resume data model
  dao/ResumeDAO.java       Database insert (with its own connection helper)
  util/PdfGenerator.java   iText PDF rendering
  util/Config.java         Properties-file reader
```

The build source directory is `src/main/java` (set in `pom.xml`), so
`src/Main.java` — a leftover hello-world stub — is not compiled.

## Dependencies

iText 7.1.15 (`kernel`, `layout`, `io`, `forms`), SLF4J 1.7.36 with Logback
1.2.10, and `org.xerial:sqlite-jdbc` 3.36.0.3.

## Known issues

Read this before you expect **Save Resume** to work.

- **The JDBC driver and the code do not match.** `ResumeDAO.java` opens a
  **MySQL** connection (`jdbc:mysql://localhost:3306/student`) but the pom only
  declares the **SQLite** driver, so there is no MySQL driver on the classpath.
  `DriverManager.getConnection` will fail with "No suitable driver" until a
  `mysql-connector-j` dependency is added.
- **Credentials are hardcoded** in `dao/ResumeDAO.java` and in the committed
  `config.properties`. The database it expects is a `resumes` table with 18
  matching columns; nothing creates that schema.
- **`config.properties` is never read by the app.** `util/Config.java` can load
  it, but no code calls `Config`, so editing the file changes nothing today.
- Build output (`target/`) and IntelliJ project files (`.idea/`) are committed,
  and `.gitignore` does not exclude them.

## License

No license file is present in this repository.