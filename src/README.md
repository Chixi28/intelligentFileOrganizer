Intelligent File Organizer – Programming WS 2024



Automates file organization based on document metadata (tags, type, access frequency) using a hierarchical tree maximizing information gain.



Overview



Input: CSV-style file describing documents with path, type, access count, and tags.



Output: Hierarchical folder tree based on tag splits, plus information gain per tag.



Goal: Make frequently accessed documents easier to find while maintaining clear structure.



Features



Supports binary, multivalued, and numeric tags.



Automatic tag derivation for specific types (image, audio, video, text, program).



Recursive document splitting using Shannon entropy / information gain.



Command-line interface for loading files, running the tree, and updating access counts.



Input Example

musik/nggyu.mp3,audio,30,genre=pop,author=Rick Astley,fun

dokumente/Abschlussaufgabe1,program,5,author=me

dokumente/Abschlussaufgabe2,program,14,author=me,fun

bilder/Oma.jpg,image,1,family



Output Example

/author=1.49

/audiogenre=1.00

/executable=0.89

/fun=0.64

/family=0.12



/author=Rick Astley/"musik/nggyu.mp3"

/author=me/fun=defined/"dokumente/Abschlussaufgabe2"

/author=me/fun=undefined/executable=defined/"dokumente/Abschlussaufgabe1"



CLI Commands



load <path> – Load input CSV file.



run <id> – Build and display folder tree for file ID.



change <id> <file> <number> – Update access count for a document.



quit – Exit program.



Notes:



Errors start with ERROR:; program continues accepting commands.



Uses Java SE 17, Checkstyle compliant, no default packages.



Quick Start

java SavySorter

> load input/example.txt

> run 0

> change 0 dokumente/Abschlussaufgabe1 10

> quit

