/*
BSD 3-Clause License

Copyright (c) 2007-2013, Distributed Computing Group (DCG)
                         ETH Zurich
                         Switzerland
                         dcg.ethz.ch
              2017-2018, André Brait

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package sinalgo.io.graphFileIO;

/**
 * Concrete Implementation of the GraphFileReaderInterface. This class loads a
 * existing GraphFile specified by the Parameter of the constructor into the
 * system and resets all the necessary configuration settings.
 */
public class GraphFileReader {
    //
    // private File file = null;
    // private SinalgoRuntime runtime = null;
    //
    // /**
    // * @param f The File to read from.
    // * @param r The instance of the SinalgoRuntime. This is needed to reset some global
    // settings at
    // * loading the graphFile.
    // */
    // public GraphFileReader(File f, SinalgoRuntime r){
    // runtime = r;
    // file = f;
    // }
    //
    // /**
    // *
    // */
    // public void read(){
    // int lineCounter = 0;
    // SinalgoRuntime.clearAllNodes();
    //
    // BufferedReader bR;
    // try {
    // bR = new BufferedReader(new FileReader(file));
    //
    // String line = bR.readLine();
    // String[] parts = line.split(";");
    //
    // //read in the dimension of the field and set the proper configuration fields
    // String dim = (parts[0].split("=")[1]);
    // String dimX = dim.split(",")[0];
    // String dimY = dim.split(",")[1];
    //
    // Configuration.dimX = Integer.parseInt(dimX);
    // Configuration.dimY = Integer.parseInt(dimY);
    //
    // //read in the type of the edges
    // String edgeType = (parts[1].split("=")[1]);
    // Configuration.setEdgeType(edgeType);
    //
    // runtime.reset();
    //
    // //first create all nodes (has to be done, because all the nodes must exist,
    // when
    // //trying to add the edges between them.)
    // String oneLine = bR.readLine();
    // while(oneLine != null){
    // String[] params = oneLine.split(";");
    // String type = params[0];
    // Node oneNode = Node.createNodeByClassname(type); // assumes fully qualified
    // type-name.
    //
    // int ID = Integer.parseInt(params[1].split("=")[1]);
    // oneNode.getID() = ID;
    //
    // double posX = Double.parseDouble(params[2].substring(5,
    // params[2].length()-1).split(",")[0]);
    // double posY = Double.parseDouble(params[2].substring(5,
    // params[2].length()-1).split(",")[1]);
    //
    // oneNode.setPosition(new Position(posX, posY, 0));
    // SinalgoRuntime.addNode(oneNode);
    // oneLine = bR.readLine();
    // lineCounter++;
    //
    // oneNode.init();
    // oneNode.checkRequirements();
    // }
    //
    // //then add all the edges
    // bR = new BufferedReader(new FileReader(file));
    //
    // lineCounter = 0;
    //
    // //skip the dimension line
    // oneLine = bR.readLine();
    //
    // oneLine = bR.readLine();
    // while(oneLine != null){
    // Enumeration<Node> nodeEnumer = SinalgoRuntime.nodes.getNodeEnumeration();
    // int ID = Integer.parseInt(oneLine.split(";")[1].split("=")[1]);
    //
    // while(nodeEnumer.hasMoreElements()){
    // Node n = nodeEnumer.nextElement();
    // if(n.getID() == ID){
    // n.deSerialize(oneLine);
    // break;
    // }
    // }
    // oneLine = bR.readLine();
    // lineCounter++;
    // }
    //
    // bR.close();
    //
    // }
    // catch (java.io.FileReadException e){
    // SinalgoRuntime.clearAllNodes();
    // Main.minorError("Error loading the graph: The specified graph-file named " +
    // file.getName() + " cannot be found:\n" + e);
    // }
    // catch (ArrayIndexOutOfBoundsException e) {
    // SinalgoRuntime.clearAllNodes();
    // Main.minorError("Error loading the graph: The specified graph-file named " +
    // file.getName() + " seems to be corrupt at line " + lineCounter + " :\n" + e);
    // }
    // catch (IOException e) {
    // Main.minorError("Error loading the graph: The specified graph-file named " +
    // file.getName() + " cannot be read:\n" + e);
    // }
    // catch (WrongConfigurationException e) {
    // Main.minorError("Error loading the graph: " + e);
    // }
    // }
}
