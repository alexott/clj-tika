# clj-tika

Clojure interface to Apache Tika project functions

## Usage

This package provides several functions in the `tika` package:

* `parse` - parses given file or stream and returns map with meta-information & text
   extracted from file.  Text is stored under key `:text`
* `detect-mime-type` - performs detection of mime type of given file or stream. Returns
   text string with mime-type
* `detect-language` - performs language detection for given text.  Returns text string
   with [ISO 639 language code](http://www.w3.org/WAI/ER/IG/ert/iso639.htm)

## Installation

Execute `lein install` to install it into local Maven repository 

## License

Copyright (c) Alex Ott, Inc. All rights reserved.

The use and distribution terms for this software are covered by the Eclipse Public License
1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can be found in the file
epl-v10.html at the root of this distribution. By using this software in any fashion, you
are agreeing to be bound by the terms of this license.
